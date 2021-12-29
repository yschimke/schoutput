package com.baulsupp.schoutput.responses

import okio.BufferedSource
import okio.Buffer

object ToStringResponseExtractor : ResponseExtractor<Any> {
  override fun mimeType(response: Any): String = "text/plain"

  override fun source(response: Any): BufferedSource = Buffer().writeUtf8(response.toString())

  override fun filename(response: Any): String? = null
}
