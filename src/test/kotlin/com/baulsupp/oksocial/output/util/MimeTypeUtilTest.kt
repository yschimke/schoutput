package com.baulsupp.oksocial.output.util

import com.baulsupp.oksocial.output.isJson
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
