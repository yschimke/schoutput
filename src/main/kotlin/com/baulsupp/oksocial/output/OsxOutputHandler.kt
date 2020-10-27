package com.baulsupp.oksocial.output

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {
  override suspend fun openPreview(response: R) {
    exec("open", "-f", "-a", "/Applications/Preview.app")
  }
}
