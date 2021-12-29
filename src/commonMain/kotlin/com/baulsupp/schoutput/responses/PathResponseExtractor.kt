package com.baulsupp.schoutput.responses

import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.buffer

class PathResponseExtractor(private val fileSystem: FileSystem) :
    ResponseExtractor<Path> {
  override fun mimeType(response: Path): String? {
    return mimeType(response)
  }

  override fun source(response: Path): BufferedSource {
    return fileSystem.source(response).buffer()
  }

  override fun filename(response: Path): String {
    return response.name
  }
}
