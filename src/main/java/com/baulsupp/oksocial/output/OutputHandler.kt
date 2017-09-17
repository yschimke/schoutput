package com.baulsupp.oksocial.output

import java.io.IOException

interface OutputHandler<in R> {
  @Throws(IOException::class)
  fun showOutput(response: R)

  fun showError(message: String?, e: Throwable?) {
    if (message != null) {
      System.err.println(message)
    }
    e?.printStackTrace()
  }

  @Throws(IOException::class)
  fun openLink(url: String) {
    System.err.println(url)
  }

  fun info(message: String) {
    println(message)
  }
}
