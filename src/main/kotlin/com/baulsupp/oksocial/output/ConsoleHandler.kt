package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.iterm.ItermOutputHandler
import com.baulsupp.oksocial.output.util.CommandUtil
import com.baulsupp.oksocial.output.util.JsonUtil.cborMapper
import com.baulsupp.oksocial.output.util.JsonUtil.jsonMapper
import com.baulsupp.oksocial.output.util.MimeTypeUtil
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isCbor
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isJson
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isMedia
import com.baulsupp.oksocial.output.util.OutputUtil
import com.baulsupp.oksocial.output.util.PlatformUtil
import com.baulsupp.oksocial.output.util.UsageException
import com.fasterxml.jackson.core.type.TypeReference
import okio.BufferedSource
import okio.Okio
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.awt.Desktop
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InterruptedIOException
import java.net.URI
import java.net.UnknownHostException
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern

open class ConsoleHandler<R>(protected var responseExtractor: ResponseExtractor<R>) : OutputHandler<R> {

  override fun showError(message: String?, e: Throwable?) {
    if (logger.isLoggable(Level.FINE)) {
      logger.log(Level.WARNING, message, e)
    }

    if (e is UsageException) {
      System.err.println(e.message)
    } else if (e is UnknownHostException && e.cause == null) {
      if (message != null) {
        System.err.print(message + ": ")
      }
      System.err.println(e.toString())
    } else {
      if (message != null) {
        System.err.println(message)
      }
      e?.printStackTrace()
    }
  }

  override fun showOutput(response: R) {
    var mimeType = responseExtractor.mimeType(response)

    var source = responseExtractor.source(response)

    if (mimeType != null) {
      if (isCbor(mimeType)) {
        source = convertCborToJson(source)
        mimeType = "application/json"
      }

      if (isMedia(mimeType)) {
        openPreview(response)
        return
      } else if (CommandUtil.isInstalled("jq") && isJson(mimeType)) {
        prettyPrintJson(source)
        return
      }
    }

    // TODO support a nice hex mode for binary files
    DownloadHandler.writeToSink(source, OutputUtil.systemOut())
    println("")
  }

  private fun convertCborToJson(source: BufferedSource): BufferedSource {
    // TODO consider adding streaming

    val cborMapper = cborMapper()

    val map = cborMapper.readValue<Any>(source.inputStream(), object : TypeReference<Map<String, Any>>() {

    })

    val om = jsonMapper()

    val bytes = om.writeValueAsBytes(map)

    return Okio.buffer(Okio.source(ByteArrayInputStream(bytes)))
  }

  private fun prettyPrintJson(response: BufferedSource) {
    val command = if (CommandUtil.isTerminal) asList("jq", "-C", ".") else asList("jq", ".")
    streamToCommand(response, command, null)
  }

  fun streamToCommand(source: BufferedSource?, command: List<String>,
                      timeout: Int?) {
    try {
      val pe = ProcessExecutor().command(command)
              .redirectOutput(System.out)
              .redirectError(Slf4jStream.ofCaller().asInfo())

      if (timeout != null) {
        pe.timeout(timeout.toLong(), TimeUnit.SECONDS)
      }

      if (source != null) {
        pe.redirectInput(source.inputStream())
      }

      val pr = pe.execute()

      if (pr.exitValue != 0) {
        throw IOException("return code " + pr.exitValue + " from " + command.joinToString(" "))
      }
    } catch (e: InterruptedException) {
      throw IOException(e)
    } catch (e: TimeoutException) {
      throw IOException(e)
    }

  }

  open fun openPreview(response: R) {
    if (Desktop.isDesktopSupported()) {
      val tempFile = writeToFile(response)

      Desktop.getDesktop().open(tempFile)
    } else {
      System.err.println("Falling back to console output, use -r to avoid warning")

      DownloadHandler.writeToSink(responseExtractor.source(response), OutputUtil.systemOut())
      println("")
    }
  }

  fun writeToFile(response: R): File {
    val mimeType = responseExtractor.mimeType(response)

    val tempFile = File.createTempFile("output", MimeTypeUtil.getExtension(mimeType))

    Okio.sink(tempFile).use { out -> DownloadHandler.writeToSink(responseExtractor.source(response), out) }
    return tempFile
  }

  override fun openLink(url: String) {
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(URI.create(url))
    } else {
      System.err.println(url)
    }
  }

  fun terminalWidth(): Int {
    try {
      val pe = ProcessExecutor().command("/bin/stty", "-a", "-f", "/dev/tty")
              .timeout(5, TimeUnit.SECONDS)
              .redirectError(Slf4jStream.ofCaller().asInfo())
              .readOutput(true)

      val output = pe.execute().outputString()

      val p = Pattern.compile("(\\d+) columns", Pattern.MULTILINE)

      val m = p.matcher(output)
      return if (m.find()) {
        Integer.parseInt(m.group(1))
      } else {
        80
      }
    } catch (ie: InterruptedException) {
      throw InterruptedIOException(ie.message)
    } catch (e: TimeoutException) {
      throw IOException(e)
    }

  }

  companion object {
    private val logger = Logger.getLogger(ConsoleHandler::class.java.name)

    fun instance(re: ResponseExtractor<*>): ConsoleHandler<*> {
      return when {
        ItermOutputHandler.isAvailable -> ItermOutputHandler(re)
        PlatformUtil.isOSX -> OsxOutputHandler(re)
        else -> ConsoleHandler(re)
      }
    }
  }
}
