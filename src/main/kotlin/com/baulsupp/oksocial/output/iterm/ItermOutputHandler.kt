package com.baulsupp.oksocial.output.iterm

import com.baulsupp.oksocial.output.formats.SvgHandler
import com.baulsupp.oksocial.output.handler.OsxOutputHandler
import com.baulsupp.oksocial.output.responses.ResponseExtractor

class ItermOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : OsxOutputHandler<R>(responseExtractor) {
  // https://www.iterm2.com/documentation-images.html
  override suspend fun openPreview(response: R) {
    val source = responseExtractor.source(response).let {
      if (responseExtractor.mimeType(response) == "image/svg+xml") {
        SvgHandler.convertSvgToPng(it)
      } else {
        it
      }
    }

    val b64 = source.readByteString().base64()

    print("${ESC}]1337;File=inline=1:")
    print(b64)
    print(BELL + "\n")
  }

  val ESC = 27.toChar()
  val BELL = 7.toChar()

  companion object {
    fun itermIsAvailable(): Boolean {
      val term = System.getenv("TERM_PROGRAM")
      val version = System.getenv("TERM_PROGRAM_VERSION")
      return "iTerm.app" == term && version != null && version.startsWith("3.")
    }
  }
}
