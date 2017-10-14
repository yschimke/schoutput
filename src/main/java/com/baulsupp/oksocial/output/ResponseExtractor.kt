package com.baulsupp.oksocial.output

import okio.BufferedSource
import java.io.IOException

interface ResponseExtractor<in R> {
    fun mimeType(response: R): String?

    @Throws(IOException::class)
    fun source(response: R): BufferedSource

    fun filename(response: R): String
}
