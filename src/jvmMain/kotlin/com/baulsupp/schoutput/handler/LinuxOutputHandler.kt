package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.responses.ResponseExtractor
import com.github.pgreze.process.Redirect.SILENT
import com.github.pgreze.process.process
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
open class LinuxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(
  responseExtractor
) {
  override suspend fun openPreview(response: R) {
    // https://stackoverflow.com/questions/5116473/linux-command-to-open-url-in-default-browser
    process("xdg-open", "-f", "-a", "/Applications/Preview.app")
  }

  override suspend fun openLink(url: String) {
    withContext(Dispatchers.IO) {
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
