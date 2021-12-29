package com.baulsupp.schoutput

import com.baulsupp.schoutput.responses.SimpleResponse
import com.baulsupp.schoutput.responses.SimpleResponseExtractor
import kotlinx.coroutines.test.runTest
import okio.ByteString.Companion.encodeUtf8
import kotlin.test.Test

class ConsoleHandlerTest {
  val output = outputHandlerInstance(SimpleResponseExtractor)

  @Test
  fun testInfo() {
    output.info("Test")
  }

  @Test
  fun testOutput() {
    runTest {
      output.showOutput(SimpleResponse(source = "text".encodeUtf8()))
    }
  }
}
