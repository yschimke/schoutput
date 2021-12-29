package com.baulsupp.schoutput.handler

import com.baulsupp.schoutput.errPrintln
import com.baulsupp.schoutput.responses.ResponseExtractor
import okio.FileSystem
import okio.Path
import okio.Sink
import okio.buffer

class DownloadHandler<in R>(
  private val responseExtractor: ResponseExtractor<R>,
  private val fileSystem: FileSystem,
  private val outputFile: Path
) : OutputHandler<R> {

  override suspend fun showOutput(response: R) {
    val source = responseExtractor.source(response)

    val outputSink = getOutputSink(response).buffer()
    try {
      outputSink.writeAll(source)
    } finally {
      outputSink.close()
    }
  }

  fun getOutputSink(response: R): Sink {
    return when {
      fileSystem.metadata(outputFile).isDirectory -> {
        val responseOutputFile = outputFile / (responseExtractor.filename(response) ?: "out.file")
        errPrintln("Saving $responseOutputFile")
        fileSystem.sink(responseOutputFile)
      }
      else -> {
        val parent = outputFile.parent
        if (parent != null && !fileSystem.exists(parent)) {
          fileSystem.createDirectory(parent)
        }
        fileSystem.sink(outputFile)
      }
    }
  }
}
