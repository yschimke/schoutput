package com.baulsupp.schoutput

import com.baulsupp.schoutput.responses.ToStringResponseExtractor
import okio.BufferedSource
import kotlin.test.Test
import kotlin.test.assertEquals

class ToStringResponseExtractorTest {
    val extractor = ToStringResponseExtractor

    @Test
    fun testToString() {
        val source: BufferedSource = extractor.source(listOf("A"))
        assertEquals("[A]", source.readUtf8())
    }
}
