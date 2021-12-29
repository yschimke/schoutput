package com.baulsupp.oksocial.output.handler

import com.baulsupp.oksocial.output.errPrintln

/**
 * Interface for command line apps to output content.
 */
interface OutputHandler<in R> {
  suspend fun showOutput(response: R)

  suspend fun showError(message: String? = null, e: Throwable? = null) {
    if (message != null) {
      errPrintln(message)
    }
    e?.printStackTrace()
  }

  suspend fun openLink(url: String) {
    errPrintln(url)
  }

  fun info(message: String) {
    println(message)
  }
}
