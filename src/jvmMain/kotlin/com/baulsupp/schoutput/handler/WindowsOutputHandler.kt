package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.UsageException
import com.baulsupp.schoutput.responses.ResponseExtractor
import com.github.pgreze.process.Redirect.SILENT
import com.github.pgreze.process.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class WindowsOutputHandler<R>(responseExtractor: ResponseExtractor<R>): ConsoleHandler<R>(responseExtractor) {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openLink(url: String) {
    val result = process(
      "rundll32", "url.dll,FileProtocolHandler", url,
      stderr = SILENT,
      stdout = SILENT
    ).resultCode

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openPreview(response: R) {
    withContext(Dispatchers.IO) {
      val tempFile = writeToFile(response)

      process(
        "powershell", "-c", tempFile.path,
        stderr = SILENT,
        stdout = SILENT
      )
    }
  }
}
