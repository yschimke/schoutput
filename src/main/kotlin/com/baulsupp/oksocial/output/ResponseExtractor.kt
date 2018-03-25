package com.baulsupp.oksocial.output

import okio.BufferedSource
import okio.Okio
import java.io.ByteArrayInputStream

object ToStringResponseExtractor : ResponseExtractor<Any> {
  override fun mimeType(response: Any): String = "text/plain"

  override fun source(response: Any): BufferedSource =
    Okio.buffer(Okio.source(ByteArrayInputStream(response.toString().toByteArray())))

  override fun filename(response: Any): String? = null
}

interface ResponseExtractor<in R> {
  fun mimeType(response: R): String?

  fun source(response: R): BufferedSource

  fun filename(response: R): String?
}
