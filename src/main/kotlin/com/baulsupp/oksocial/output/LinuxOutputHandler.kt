package com.baulsupp.oksocial.output

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class LinuxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {
  override suspend fun openPreview(response: R) {
    // https://stackoverflow.com/questions/5116473/linux-command-to-open-url-in-default-browser
    exec("xdg-open", "-f", "-a", "/Applications/Preview.app")
  }

  override suspend fun openLink(url: String) {
    GlobalScope.launch {
      execResult("xdg-open", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)
    }
    // terrible but allows for the link to likely load before we exit the process or similar
    // without waiting indefinitely for the process to end
    delay(10)
  }
}
