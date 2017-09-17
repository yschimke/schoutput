package com.baulsupp.oksocial.output

import okio.BufferedSource
import java.io.IOException
import java.util.*

interface ResponseExtractor<in R> {
  fun mimeType(response: R): Optional<String>

  @Throws(IOException::class)
  fun source(response: R): BufferedSource

  fun filename(response: R): String
}
