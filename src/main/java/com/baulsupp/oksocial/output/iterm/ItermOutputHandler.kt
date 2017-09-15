package com.baulsupp.oksocial.output.iterm

import com.baulsupp.oksocial.output.OsxOutputHandler
import com.baulsupp.oksocial.output.ResponseExtractor
import java.io.IOException

class ItermOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : OsxOutputHandler<R>(responseExtractor) {

  // https://www.iterm2.com/documentation-images.html
  @Throws(IOException::class)
  override fun openPreview(response: R) {
    val b64 = responseExtractor.source(response).readByteString().base64()

    print(ESC + "]1337;File=inline=1:")
    print(b64)
    print(BELL + "\n")
  }

  companion object {
    val ESC = 27.toChar()
    val BELL = 7.toChar()

    val isAvailable: Boolean
      get() {
        val term = System.getenv("TERM_PROGRAM")
        val version = System.getenv("TERM_PROGRAM_VERSION")
        return "iTerm.app" == term && version != null && version.startsWith("3.")
      }
  }
}
