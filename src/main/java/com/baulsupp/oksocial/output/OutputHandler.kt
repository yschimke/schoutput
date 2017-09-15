package com.baulsupp.oksocial.output

import java.io.IOException

interface OutputHandler<R> {
  @Throws(IOException::class)
  fun showOutput(response: R)

  open fun showError(message: String?, e: Throwable?) {
    if (message != null) {
      System.err.println(message)
    }
    e?.printStackTrace()
  }

  @Throws(IOException::class)
  open fun openLink(url: String) {
    System.err.println(url)
  }

  open fun info(message: String) {
    println(message)
  }
}
