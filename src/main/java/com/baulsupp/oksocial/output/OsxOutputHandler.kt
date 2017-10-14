package com.baulsupp.oksocial.output

import java.io.IOException

import java.util.Arrays.asList

open class OsxOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : ConsoleHandler<R>(responseExtractor) {

    @Throws(IOException::class)
    override fun openPreview(response: R) {
        streamToCommand(responseExtractor.source(response),
                asList("open", "-f", "-a", "/Applications/Preview.app"), 30)
    }
}
