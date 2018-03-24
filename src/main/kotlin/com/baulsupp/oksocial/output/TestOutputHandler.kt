package com.baulsupp.oksocial.output

class TestOutputHandler<R> : OutputHandler<R> {
  val responses: MutableList<R> = mutableListOf()
  val failures: MutableList<Throwable> = mutableListOf()
  val stdout: MutableList<String> = mutableListOf()

  override suspend fun showOutput(response: R) {
    responses.add(response)
  }

  override suspend fun showError(message: String?, e: Throwable?) {
    if (e != null) {
      failures.add(e)
    }
  }

  override fun info(message: String) {
    stdout.add(message)
  }
}
