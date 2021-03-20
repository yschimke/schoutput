package com.baulsupp.oksocial.output.handler

import com.baulsupp.oksocial.output.UsageException
import com.baulsupp.oksocial.output.responses.ResponseExtractor
import com.github.pgreze.process.InputSource
import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import kotlinx.coroutines.ExperimentalCoroutinesApi

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(
  responseExtractor
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openPreview(response: R) {
    println("a")
    responseExtractor.source(response).use {
      process(
        "open", "-f", "-a", "Preview",
        stdin = InputSource.fromInputStream(it.inputStream()),
        stderr = Redirect.PRINT,
        stdout = Redirect.SILENT
      )
    }
    println("b")
  }

  @OptIn(ExperimentalCoroutinesApi::class)
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
