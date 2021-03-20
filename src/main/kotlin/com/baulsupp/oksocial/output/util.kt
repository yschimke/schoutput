package com.baulsupp.oksocial.output

import com.github.pgreze.process.Redirect.SILENT
import com.github.pgreze.process.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.Sink
import okio.sink
import java.io.Console
import java.io.IOException
import java.io.OutputStream
import java.util.Properties
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

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun isInstalled(command: String): Boolean = if (com.baulsupp.oksocial.output.isOSX) {
  process("command", "-v", command, stdout = SILENT, stderr = SILENT).resultCode == 0
} else if (!isWindows) {
  process("which", command, stdout = SILENT, stderr = SILENT).resultCode == 0
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

object NullOutputStream : OutputStream() {
  override fun write(b: Int) {
  }

  override fun write(b: ByteArray) {
  }

  override fun write(b: ByteArray, off: Int, len: Int) {
  }
}
