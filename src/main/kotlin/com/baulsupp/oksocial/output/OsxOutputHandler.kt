package com.baulsupp.oksocial.output

import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import kotlinx.coroutines.ExperimentalCoroutinesApi

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(
  responseExtractor
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun openPreview(response: R) {
    process(
      "open", "-f", "-a", "/Applications/Preview.app",
      stderr = Redirect.SILENT,
      stdout = Redirect.SILENT
    )
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
