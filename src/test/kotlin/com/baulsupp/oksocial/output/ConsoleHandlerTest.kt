package com.baulsupp.oksocial.output

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import okio.BufferedSource
import okio.Okio
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.LinkedHashMap

class ConsoleHandlerTest {
  @Test
  @Throws(IOException::class)
  fun testCborSupport() {
    val cborMapper = ObjectMapper(CBORFactory())

    val s = LinkedHashMap<String, String>()
    s["a"] = "AAA"
    s["b"] = "BBB"
    val bytes = cborMapper.writeValueAsBytes(s)

    val extractor = object : ResponseExtractor<ByteArray> {
      override fun mimeType(response: ByteArray): String? {
        return "application/cbor"
      }

      @Throws(IOException::class)
      override fun source(response: ByteArray): BufferedSource {
        return Okio.buffer(Okio.source(ByteArrayInputStream(response)))
      }

      override fun filename(response: ByteArray): String {
        return "filename"
      }
    }
    val c = ConsoleHandler(extractor)
    c.showOutput(bytes)
  }
}
