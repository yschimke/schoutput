package com.baulsupp.oksocial.output.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MimeTypeUtilTest {
  @Test
  fun testJson() {
    assertTrue(MimeTypeUtil.isJson("application/json;charset=utf-8"))
    assertTrue(MimeTypeUtil.isJson("application/json"))
    assertTrue(MimeTypeUtil.isJson("text/json"))
    assertFalse(MimeTypeUtil.isJson("text/plain"))
    assertFalse(MimeTypeUtil.isJson("AAAAA"))
  }
}
