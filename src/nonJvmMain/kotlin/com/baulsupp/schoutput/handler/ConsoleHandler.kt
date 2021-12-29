package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.UsageException
import com.baulsupp.schoutput.errPrintln
import com.baulsupp.schoutput.responses.ResponseExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class ConsoleHandler<R>(protected var responseExtractor: ResponseExtractor<R>) : OutputHandler<R> {
  override suspend fun showError(message: String?, e: Throwable?) {
    if (e is UsageException) {
      errPrintln(e.message)
    } else {
      if (message != null) {
        errPrintln(message)
      }
      e?.printStackTrace()
    }
  }

  override suspend fun showOutput(response: R) {
    withContext(Dispatchers.Default) {
      val source = responseExtractor.source(response)

      // TODO optimise?
      println(source.readUtf8())
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun openLink(url: String) {
    errPrintln(url)
  }
}
