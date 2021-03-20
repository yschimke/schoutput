package com.baulsupp.oksocial.output.responses

import okio.Buffer
import okio.BufferedSource

object SimpleResponseExtractor : ResponseExtractor<SimpleResponse> {
  override fun mimeType(response: SimpleResponse): String? = response.mimeType

  override fun source(response: SimpleResponse): BufferedSource = Buffer().apply {
    write(
      response.source
    )
  }

  override fun filename(response: SimpleResponse): String? = response.filename
}
