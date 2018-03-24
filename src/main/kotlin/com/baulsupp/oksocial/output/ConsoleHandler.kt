package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.iterm.ItermOutputHandler
import com.baulsupp.oksocial.output.process.exec
import com.baulsupp.oksocial.output.process.stdErrLogging
import com.baulsupp.oksocial.output.util.CommandUtil
import com.baulsupp.oksocial.output.util.MimeTypeUtil
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isCbor
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isJson
import com.baulsupp.oksocial.output.util.MimeTypeUtil.isMedia
import com.baulsupp.oksocial.output.util.OutputUtil
import com.baulsupp.oksocial.output.util.PlatformUtil
import com.baulsupp.oksocial.output.util.UsageException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import kotlinx.coroutines.experimental.runBlocking
import okio.BufferedSource
import okio.Okio
import java.awt.Desktop
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.UnknownHostException
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

open class ConsoleHandler<R>(protected var responseExtractor: ResponseExtractor<R>) : OutputHandler<R> {
  val jqInstalled by lazy {
    runBlocking {
      CommandUtil.isInstalled("jq")
    }
  }

  override suspend fun showError(message: String?, e: Throwable?) {
    if (logger.isLoggable(Level.FINE)) {
      logger.log(Level.WARNING, message, e)
    }

    if (e is UsageException) {
      System.err.println(e.message)
    } else if (e is UnknownHostException && e.cause == null) {
      if (message != null) {
        System.err.print("$message: ")
      }
      System.err.println(e.toString())
    } else {
      if (message != null) {
        System.err.println(message)
      }
      e?.printStackTrace()
    }
  }

  override suspend fun showOutput(response: R) {
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
      } else if (jqInstalled && isJson(mimeType)) {
        prettyPrintJson(source)
        return
      }
    }

    // TODO support a nice hex mode for binary files
    DownloadHandler.writeToSink(source, OutputUtil.systemOut())
    println("")
  }

  private fun cborMapper(): ObjectMapper {
    return ObjectMapper(CBORFactory()).registerModule(ParameterNamesModule())
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
  }

  private fun jsonMapper(): ObjectMapper {
    return ObjectMapper().registerModule(ParameterNamesModule())
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
  }

  private fun convertCborToJson(source: BufferedSource): BufferedSource {
    // TODO consider adding streaming

    val cborMapper = cborMapper()

    val map = cborMapper.readValue<Any>(source.inputStream(),
      object : TypeReference<Map<String, Any>>() {})

    val om = jsonMapper()

    val bytes = om.writeValueAsBytes(map)

    return Okio.buffer(Okio.source(ByteArrayInputStream(bytes)))
  }

  suspend fun prettyPrintJson(response: BufferedSource) {
    val command = if (CommandUtil.isTerminal) asList("jq", "-C", ".") else asList("jq", ".")

    val result = exec(command) {
      redirectInput(response.inputStream())
      redirectError(stdErrLogging)
      redirectOutput(System.out)
    }

    if (!result.success) {
      throw IOException("return code " + result.exitCode + " from " + command.joinToString(" "))
    }
  }

  open suspend fun openPreview(response: R) {
    if (Desktop.isDesktopSupported()) {
      val tempFile = writeToFile(response)

      Desktop.getDesktop().open(tempFile)
    } else {
      System.err.println("Falling back to console output, use -r to avoid warning")

      DownloadHandler.writeToSink(responseExtractor.source(response), OutputUtil.systemOut())
      println("")
    }
  }

  suspend fun writeToFile(response: R): File {
    val mimeType = responseExtractor.mimeType(response)

    val tempFile = File.createTempFile("output", MimeTypeUtil.getExtension(mimeType))

    Okio.sink(tempFile).use { out ->
      DownloadHandler.writeToSink(responseExtractor.source(response), out)
    }
    return tempFile
  }

  override suspend fun openLink(url: String) {
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(URI.create(url))
    } else {
      System.err.println(url)
    }
  }

  suspend fun terminalWidth(): Int {
    val command = if (PlatformUtil.isOSX)
      listOf("/bin/stty", "-a", "-f", "/dev/tty")
    else
      listOf("/bin/stty", "-a", "-F", "/dev/tty")

    val result = exec(command) {
      timeout(5, TimeUnit.SECONDS)
      redirectError(stdErrLogging)
      readOutput(true)
    }

    val matchResult = "(\\d+) columns".toRegex(RegexOption.MULTILINE).find(result.outputString)
    return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 80
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
