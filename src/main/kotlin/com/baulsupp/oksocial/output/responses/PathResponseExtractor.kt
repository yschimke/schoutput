package com.baulsupp.oksocial.output.responses

import okio.BufferedSource
import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path
import okio.buffer
import java.nio.file.Files

@OptIn(ExperimentalFileSystem::class)
class PathResponseExtractor(private val fileSystem: FileSystem = FileSystem.SYSTEM) :
  ResponseExtractor<Path> {
  override fun mimeType(response: Path): String? {
    // TODO check if fileSystem is SYSTEM
    return Files.probeContentType(response.toNioPath())
  }

  override fun source(response: Path): BufferedSource {
    return fileSystem.source(response).buffer()
  }

  override fun filename(response: Path): String {
    return response.name
  }

  companion object {
    val SYSTEM = PathResponseExtractor(FileSystem.SYSTEM)
  }
}
