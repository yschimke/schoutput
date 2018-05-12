package com.baulsupp.oksocial.output

import okio.BufferedSource
import okio.Okio
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files

object ToStringResponseExtractor : ResponseExtractor<Any> {
  override fun mimeType(response: Any): String = "text/plain"

  override fun source(response: Any): BufferedSource =
    Okio.buffer(Okio.source(ByteArrayInputStream(response.toString().toByteArray())))

  override fun filename(response: Any): String? = null
}

object FileResponseExtractor : ResponseExtractor<File> {
  override fun mimeType(response: File): String? {
    return Files.probeContentType(response.toPath())
  }

  override fun source(response: File): BufferedSource {
    return Okio.buffer(Okio.source(response))
  }

  override fun filename(response: File): String {
    return response.name
  }
}

interface ResponseExtractor<in R> {
  fun mimeType(response: R): String?

  fun source(response: R): BufferedSource

  fun filename(response: R): String?
}
