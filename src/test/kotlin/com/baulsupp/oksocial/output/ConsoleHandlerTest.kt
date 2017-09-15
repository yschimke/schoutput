package com.baulsupp.oksocial.output

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.util.LinkedHashMap
import java.util.Optional
import okio.BufferedSource
import okio.Okio
import org.junit.Test

import java.util.Optional.of

class ConsoleHandlerTest {
  @Test
  @Throws(IOException::class)
  fun testCborSupport() {
    val cborMapper = ObjectMapper(CBORFactory())

    val s = LinkedHashMap<String, String>()
    s.put("a", "AAA")
    s.put("b", "BBB")
    val bytes = cborMapper.writeValueAsBytes(s)

    val extractor = object : ResponseExtractor<ByteArray> {
      override fun mimeType(response: ByteArray): Optional<String> {
        return of("application/cbor")
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
