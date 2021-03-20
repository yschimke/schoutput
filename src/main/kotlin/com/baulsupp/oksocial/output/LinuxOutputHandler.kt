package com.baulsupp.oksocial.output

import com.github.pgreze.process.Redirect
import com.github.pgreze.process.Redirect.SILENT
import com.github.pgreze.process.process
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class LinuxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(
  responseExtractor
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openPreview(response: R) {
    // https://stackoverflow.com/questions/5116473/linux-command-to-open-url-in-default-browser
    process("xdg-open", "-f", "-a", "/Applications/Preview.app")
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openLink(url: String) {
    GlobalScope.launch {
      process(
        "xdg-open", url,
        stderr = SILENT,
        stdout = SILENT
      )
    }
    // terrible but allows for the link to likely load before we exit the process or similar
    // without waiting indefinitely for the process to end
    delay(10)
  }
}
