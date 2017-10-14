package com.baulsupp.oksocial.output

import com.google.common.collect.Lists
import java.io.IOException

class TestOutputHandler<R> : OutputHandler<R> {
    val responses: MutableList<R> = Lists.newArrayList()
    val failures: MutableList<Throwable> = Lists.newArrayList()
    val stdout: MutableList<String> = Lists.newArrayList()

    @Throws(IOException::class)
    override fun showOutput(response: R) {
        responses.add(response)
    }

    override fun showError(message: String?, e: Throwable?) {
        if (e != null) {
            failures.add(e)
        }
    }

    override fun info(message: String) {
        stdout.add(message)
    }
}
