package com.baulsupp.oksocial.output

open class LinuxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {
  override suspend fun openPreview(response: R) {
    // https://stackoverflow.com/questions/5116473/linux-command-to-open-url-in-default-browser
    exec("xdg-open", "-f", "-a", "/Applications/Preview.app")
  }

  override suspend fun openLink(url: String) {
    val result = execResult("xdg-open", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }
}
