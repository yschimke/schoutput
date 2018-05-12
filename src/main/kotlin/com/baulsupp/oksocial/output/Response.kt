package com.baulsupp.oksocial.output

import okio.Buffer
import okio.BufferedSource
import okio.ByteString

data class SimpleResponse(val mimeType: String? = null, val source: ByteString = ByteString.EMPTY, val filename: String? = null)

object SimpleResponseExtractor : ResponseExtractor<SimpleResponse> {
  override fun mimeType(response: SimpleResponse): String? = response.mimeType

  override fun source(response: SimpleResponse): BufferedSource = Buffer().apply { write(response.source) }

  override fun filename(response: SimpleResponse): String? = response.filename
}
