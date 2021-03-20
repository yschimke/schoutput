package com.baulsupp.oksocial.output.responses

import okio.BufferedSource
import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import java.io.File
import java.nio.file.Files

@OptIn(ExperimentalFileSystem::class)
object FileResponseExtractor : ResponseExtractor<File> {
  override fun mimeType(response: File): String? {
    return Files.probeContentType(response.toPath())
  }

  override fun source(response: File): BufferedSource {
    return FileSystem.SYSTEM.source(response.toOkioPath()).buffer()
  }

  override fun filename(response: File): String {
    return response.name
  }
}
