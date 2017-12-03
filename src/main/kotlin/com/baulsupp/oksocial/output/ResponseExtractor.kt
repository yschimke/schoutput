package com.baulsupp.oksocial.output

import okio.BufferedSource

interface ResponseExtractor<in R> {
  fun mimeType(response: R): String?

  fun source(response: R): BufferedSource

  fun filename(response: R): String
}
