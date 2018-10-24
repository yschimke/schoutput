package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.process.exec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okio.BufferedSource
import okio.Sink
import okio.sink
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.io.Console
import java.io.IOException
import java.util.Properties
import javax.activation.MimeType
import javax.activation.MimeTypeParseException

fun BufferedSource.writeToSink(out: Sink) {
  while (!this.exhausted()) {
    out.write(this.buffer, this.buffer.size)
    out.flush()
  }
}

val systemOut: Sink by lazy {
  System.out.sink()
}

val stdErrLogging = Slf4jStream.ofCaller().asInfo()!!

val isTerminal by lazy {
  System.console() != null
}

suspend fun Console.readPasswordString(prompt: String): String {
  return GlobalScope.async(Dispatchers.IO) {
    String(readPassword(prompt))
  }.await()
}

suspend fun Console.readString(prompt: String): String {
  return GlobalScope.async(Dispatchers.IO) {
    readLine(prompt)
  }.await()
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

fun isMediaType(mediaType: String?, vararg types: String): Boolean {
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

fun versionString(mainClass: Class<*>, propertiesFile: String): String {
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

val isOSX by lazy { System.getProperty("os.name").contains("OS X") }

val isLinux by lazy { System.getProperty("os.name").contains("Linux") }
