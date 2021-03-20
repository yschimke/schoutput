package com.baulsupp.oksocial.output.responses

import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream

object ToStringResponseExtractor : ResponseExtractor<Any> {
  override fun mimeType(response: Any): String = "text/plain"

  override fun source(response: Any): BufferedSource =
    ByteArrayInputStream(response.toString().toByteArray()).source().buffer()

  override fun filename(response: Any): String? = null
}
