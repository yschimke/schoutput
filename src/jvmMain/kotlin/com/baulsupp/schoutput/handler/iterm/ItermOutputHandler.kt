package com.baulsupp.schoutput.handler.iterm

import com.baulsupp.schoutput.handler.OsxOutputHandler
import com.baulsupp.schoutput.responses.ResponseExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItermOutputHandler<R>(responseExtractor: ResponseExtractor<R>) : OsxOutputHandler<R>(responseExtractor) {
  // https://www.iterm2.com/documentation-images.html
  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun openPreview(response: R) {
    withContext(Dispatchers.IO) {
      val source = responseExtractor.source(response)

      val b64 = source.readByteString().base64()

      print("${ESC}]1337;File=inline=1:")
      print(b64)
      print(BELL + "\n")
    }
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
