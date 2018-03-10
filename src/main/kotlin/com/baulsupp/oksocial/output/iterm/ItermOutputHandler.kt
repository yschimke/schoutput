package com.baulsupp.oksocial.output.iterm

import com.baulsupp.oksocial.output.OsxOutputHandler
import com.baulsupp.oksocial.output.ResponseExtractor
import com.baulsupp.oksocial.output.formats.SvgHandler

class ItermOutputHandler<R>(responseExtractor: ResponseExtractor<R>) :
  OsxOutputHandler<R>(responseExtractor) {

  // https://www.iterm2.com/documentation-images.html
  override fun openPreview(response: R) {
    val source = responseExtractor.source(response).let {
      if (responseExtractor.mimeType(response) == "image/svg+xml") {
        SvgHandler.convertSvgToPng(it)
      } else {
        it
      }
    }

    val b64 = source.readByteString().base64()

    print("$ESC]1337;File=inline=1:")
    print(b64)
    print(BELL + "\n")
  }

  companion object {
    const val ESC = 27.toChar()
    const val BELL = 7.toChar()

    val isAvailable: Boolean
      get() {
        val term = System.getenv("TERM_PROGRAM")
        val version = System.getenv("TERM_PROGRAM_VERSION")
        return "iTerm.app" == term && version != null && version.startsWith("3.")
      }
  }
}
