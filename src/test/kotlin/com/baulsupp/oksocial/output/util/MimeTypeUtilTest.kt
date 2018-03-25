package com.baulsupp.oksocial.output.util

import com.baulsupp.oksocial.output.isJson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MimeTypeUtilTest {
  @Test
  fun testJson() {
    assertTrue(isJson("application/json;charset=utf-8"))
    assertTrue(isJson("application/json"))
    assertTrue(isJson("text/json"))
    assertFalse(isJson("text/plain"))
    assertFalse(isJson("AAAAA"))
  }
}
