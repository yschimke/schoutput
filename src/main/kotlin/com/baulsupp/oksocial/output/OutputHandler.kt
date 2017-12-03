package com.baulsupp.oksocial.output

interface OutputHandler<in R> {
  fun showOutput(response: R)

  fun showError(message: String? = null, e: Throwable? = null) {
    if (message != null) {
      System.err.println(message)
    }
    e?.printStackTrace()
  }

  fun openLink(url: String) {
    System.err.println(url)
  }

  fun info(message: String) {
    println(message)
  }
}
