package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.UsageException
import com.baulsupp.schoutput.responses.ResponseExtractor
import com.github.pgreze.process.InputSource
import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(
  responseExtractor
) {
  override suspend fun openPreview(response: R) {
    responseExtractor.source(response).use {
      process(
        "open", "-f", "-a", "Preview",
        stdin = InputSource.fromInputStream(it.inputStream()),
        stderr = Redirect.PRINT,
        stdout = Redirect.SILENT
      )
    }
  }

  override suspend fun openLink(url: String) {
    val result = process(
      "open", url,
      stderr = Redirect.SILENT,
      stdout = Redirect.SILENT
    ).resultCode

    if (result != 0) {
      throw UsageException("open url failed: $url")
    }
  }
}
