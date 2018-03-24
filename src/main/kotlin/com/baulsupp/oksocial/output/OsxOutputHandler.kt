package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.process.exec
import com.baulsupp.oksocial.output.process.stdErrLogging
import java.util.concurrent.TimeUnit

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {

  override suspend fun openPreview(response: R) {
    exec(listOf("open", "-f", "-a", "/Applications/Preview.app")) {
      redirectError(stdErrLogging)
      redirectOutput(stdErrLogging)
      timeout(30, TimeUnit.SECONDS)
    }
  }
}
