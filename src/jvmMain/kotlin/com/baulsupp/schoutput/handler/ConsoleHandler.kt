package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.*
import com.baulsupp.schoutput.responses.ResponseExtractor
import com.github.pgreze.process.InputSource.Companion.fromInputStream
import com.github.pgreze.process.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.sink
import java.awt.Desktop
import java.io.BufferedInputStream
import java.io.File
import java.net.URI
import java.net.UnknownHostException
import java.util.logging.Level
import java.util.logging.Logger
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import kotlin.coroutines.resume

open class ConsoleHandler<R>(protected var responseExtractor: ResponseExtractor<R>) : OutputHandler<R> {
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
    withContext(Dispatchers.IO) {
      val mimeType = responseExtractor.mimeType(response)
      val source = responseExtractor.source(response)

      if (mimeType != null) {
        if (isMedia(mimeType)) {
          openPreview(response)
          return@withContext
        } else if (isAudio(mimeType)) {
          playAudio(response)
          return@withContext
        } else if (jqInstalled && isJson(mimeType)) {
          prettyPrintJson(source)
          return@withContext
        }
      }

      source.writeToSink(systemOut)
      println("")
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun prettyPrintJson(response: BufferedSource) {
    val command = if (isTerminal) arrayOf("jq", "-C", ".") else arrayOf("jq", ".")
    response.inputStream().use {
      process(
        *command,
        stdin = fromInputStream(it)
      )
    }
  }

  open suspend fun playAudio(response: R) {
    val source = responseExtractor.source(response)

    // TODO proper IO handling continuations style also
    source.inputStream().use {
      return kotlinx.coroutines.suspendCancellableCoroutine { c ->
        val audioIn = AudioSystem.getAudioInputStream(BufferedInputStream(it, 512 * 1024))
        val clip = AudioSystem.getClip()
        clip.open(audioIn)
        clip.addLineListener { event ->
          if (event.type == LineEvent.Type.STOP) {
            c.resume(Unit)
          }
        }
        clip.start()
      }
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  open suspend fun openPreview(response: R) {
    withContext(Dispatchers.IO) {
      if (Desktop.isDesktopSupported()) {
        val tempFile = writeToFile(response)

        Desktop.getDesktop().open(tempFile)
      } else {
        System.err.println("Falling back to console output, use -r to avoid warning")

        responseExtractor.source(response).writeToSink(systemOut)
        println("")
      }
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun writeToFile(response: R): File {
    return withContext(Dispatchers.IO) {
      val mimeType = responseExtractor.mimeType(response)

      val tempFile = File.createTempFile("output", getExtension(mimeType))

      tempFile.sink().use { out ->
        responseExtractor.source(response).writeToSink(out)
      }

      tempFile
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun openLink(url: String) {
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(URI.create(url))
    } else {
      System.err.println(url)
    }
  }

  companion object {
    private val logger = Logger.getLogger(ConsoleHandler::class.java.name)
  }
}
