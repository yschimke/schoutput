package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.process.exec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.Sink
import okio.sink
import org.zeroturnaround.exec.stream.LogOutputStream
import java.io.Console
import java.io.IOException
import java.util.*
import java.util.logging.Logger
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

val stdErrLogging by lazy {
  object : LogOutputStream() {
    override fun processLine(line: String) {
      Logger.getLogger("STDERR").fine(line)
    }
  }
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
  exec("command", "-v", command).success
} else {
  exec("which", command).success
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
  System.getProperty("os.name")
    .contains("OS X")
}

val isLinux by lazy {
  System.getProperty("os.name")
    .contains("Linux")
}
