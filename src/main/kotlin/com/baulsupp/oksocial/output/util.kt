package com.baulsupp.oksocial.output

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.Sink
import okio.sink
import java.io.Console
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Properties
import java.util.concurrent.TimeUnit
import javax.activation.MimeType
import javax.activation.MimeTypeParseException

suspend fun BufferedSource.writeToSink(out: Sink) {
  withContext(Dispatchers.IO) {
    while (!this@writeToSink.exhausted()) {
      out.write(this@writeToSink.buffer, this@writeToSink.buffer.size)
      out.flush()
    }
  }
}

val systemOut: Sink by lazy {
  System.out.sink()
}

val isTerminal by lazy {
  System.console() != null
}

suspend fun Console.readPasswordString(prompt: String): String {
  return withContext(Dispatchers.IO) {
    String(readPassword(prompt))
  }
}

suspend fun Console.readString(prompt: String): String {
  return withContext(Dispatchers.IO) {
    readLine(prompt)
  }
}

suspend fun isInstalled(command: String): Boolean = if (isOSX) {
  execResult("command", "-v", command, outputMode = ConsoleHandler.Companion.OutputMode.Hide) == 0
} else if (!isWindows) {
  execResult("which", command, outputMode = ConsoleHandler.Companion.OutputMode.Hide) == 0
} else {
  false
}


fun isMedia(mediaType: String): Boolean {
  return mediaType.startsWith("image/") || mediaType.endsWith("/pdf")
}

fun isAudio(mediaType: String): Boolean {
  return mediaType.startsWith("audio/")
}

fun getExtension(mediaType: String?): String = when (mediaType) {
  "image/jpeg" -> ".jpg"
  "image/gif" -> ".gif"
  "image/png" -> ".png"
  else -> ".data"
}

fun isCbor(mediaType: String): Boolean {
  return isMediaType(mediaType, "application/cbor") || mediaType
    .endsWith("+cbor")
}

fun isJson(mediaType: String): Boolean {
  return isMediaType(mediaType, "application/json", "text/json") || mediaType
    .endsWith("+json")
}

fun isMediaType(
  mediaType: String?,
  vararg types: String
): Boolean {
  return try {
    if (mediaType == null) {
      return false
    }

    val t = MimeType(mediaType)

    types.any { t.match(it) }
  } catch (e: MimeTypeParseException) {
    false
  }
}

fun versionString(
  mainClass: Class<*>,
  propertiesFile: String
): String {
  return try {
    val prop = Properties()
    val `in` = mainClass.getResourceAsStream(propertiesFile)
    prop.load(`in`)
    `in`.close()
    prop.getProperty("version")
  } catch (e: IOException) {
    throw AssertionError("Could not load $propertiesFile")
  }
}

val isOSX by lazy {
  System.getProperty("os.name").contains("OS X")
}

val isLinux by lazy {
  System.getProperty("os.name").contains("Linux")
}

val isWindows by lazy {
  System.getProperty("os.name").contains("Windows")
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun exec(vararg commandLine: String, outputMode: ConsoleHandler.Companion.OutputMode = ConsoleHandler.Companion.OutputMode.Hide, inputStream: InputStream? = null): String? {
  var result: String? = null

  withContext(Dispatchers.IO) {
    val process = ProcessBuilder(*commandLine)
      .apply {
        when (outputMode) {
            ConsoleHandler.Companion.OutputMode.Inherit -> {
              redirectError(ProcessBuilder.Redirect.INHERIT)
              redirectOutput(ProcessBuilder.Redirect.INHERIT)
            }
            ConsoleHandler.Companion.OutputMode.Return -> {
              redirectError(ProcessBuilder.Redirect.INHERIT)
              redirectOutput(ProcessBuilder.Redirect.PIPE)
            }
            ConsoleHandler.Companion.OutputMode.Hide -> {
              redirectErrorStream(true)
              redirectOutput(ProcessBuilder.Redirect.PIPE)
            }
        }
      }
      .start()

    if (inputStream != null) {
      launch {
        inputStream.use { input ->
          process.outputStream.use { output ->
            input.copyTo(output)
          }
        }
      }
    } else {
      process.outputStream.close()
    }

    if (outputMode == ConsoleHandler.Companion.OutputMode.Hide) {
      launch {
        process.inputStream.copyTo(NullOutputStream)
      }
    } else if (outputMode == ConsoleHandler.Companion.OutputMode.Return) {
      process.errorStream.copyTo(System.err)
      result = process.inputStream.bufferedReader().use {
        it.readText()
      }
    }

    process.waitFor(30, TimeUnit.SECONDS)
  }

  return result
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun execResult(vararg commandLine: String, outputMode: ConsoleHandler.Companion.OutputMode = ConsoleHandler.Companion.OutputMode.Hide): Int {
  return withContext(Dispatchers.IO) {
    val process = ProcessBuilder(*commandLine)
      .apply {
        when (outputMode) {
          ConsoleHandler.Companion.OutputMode.Inherit -> {
            redirectError(ProcessBuilder.Redirect.INHERIT)
            redirectOutput(ProcessBuilder.Redirect.INHERIT)
          }
          ConsoleHandler.Companion.OutputMode.Hide -> {
            redirectErrorStream(true)
            redirectOutput(ProcessBuilder.Redirect.PIPE)
          }
          ConsoleHandler.Companion.OutputMode.Return -> {
            error("Unsupported Mode: $outputMode")
          }
        }
      }
      .start()

    if (outputMode == ConsoleHandler.Companion.OutputMode.Hide) {
      launch {
        process.inputStream.copyTo(NullOutputStream)
      }
    }

    process
      .waitFor()
  }
}

object NullOutputStream : OutputStream() {
  override fun write(b: Int) {
  }

  override fun write(b: ByteArray) {
  }

  override fun write(b: ByteArray, off: Int, len: Int) {
  }
}
