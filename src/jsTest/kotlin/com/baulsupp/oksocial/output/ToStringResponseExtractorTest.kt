package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.responses.ToStringResponseExtractor
import kotlin.test.Test
import kotlin.test.assertEquals
import okio.BufferedSource

class ToStringResponseExtractorTest {
    val extractor = ToStringResponseExtractor

    @Test
    fun testToString() {
        val source: BufferedSource = extractor.source(listOf("A"))
        assertEquals("[A]", source.readUtf8())
    }
}