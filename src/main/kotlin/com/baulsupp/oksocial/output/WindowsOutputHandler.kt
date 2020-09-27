package com.baulsupp.oksocial.output

class WindowsOutputHandler<R>(responseExtractor: ResponseExtractor<R>): ConsoleHandler<R>(responseExtractor) {
  override suspend fun openLink(url: String) {
    val result = execResult("rundll32", "url.dll,FileProtocolHandler", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }
}

suspend fun main() {
  WindowsOutputHandler(SimpleResponseExtractor).openLink("https://www.google.com")
}
