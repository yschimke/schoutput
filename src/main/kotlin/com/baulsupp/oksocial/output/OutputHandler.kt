package com.baulsupp.oksocial.output

/**
 * Interface for command line apps to output content.
 */
interface OutputHandler<in R> {
  suspend fun showOutput(response: R)

  suspend fun showError(message: String? = null, e: Throwable? = null) {
    if (message != null) {
      System.err.println(message)
    }
    e?.printStackTrace()
  }

  suspend fun openLink(url: String) {
    System.err.println(url)
  }

  fun info(message: String) {
    println(message)
  }
}
