package com.baulsupp.oksocial.output

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.toByteString
import java.io.File

class WindowsOutputHandler<R>(responseExtractor: ResponseExtractor<R>): ConsoleHandler<R>(responseExtractor) {
  override suspend fun openLink(url: String) {
    val result = execResult("rundll32", "url.dll,FileProtocolHandler", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }

  override suspend fun openPreview(response: R) {
    withContext(Dispatchers.IO) {
      val tempFile = writeToFile(response)

      execResult("powershell", "-c", tempFile.path)
    }
  }
}

suspend fun main() {
  WindowsOutputHandler(SimpleResponseExtractor).openPreview(
    SimpleResponse("image/png", source = File("src/test/resources/PNG_transparency_demonstration_1.png").readBytes().toByteString()))
}
