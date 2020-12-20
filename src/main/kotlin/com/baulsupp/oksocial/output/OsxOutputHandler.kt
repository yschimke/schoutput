package com.baulsupp.oksocial.output

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {
  override suspend fun openPreview(response: R) {
    exec("open", "-f", "-a", "/Applications/Preview.app")
  }

  override suspend fun openLink(url: String) {
    val result = execResult("open", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }
}
