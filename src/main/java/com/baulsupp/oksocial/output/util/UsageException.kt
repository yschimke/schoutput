package com.baulsupp.oksocial.output.util

class UsageException : RuntimeException {
  constructor(msg: String) : super(msg) {}

  constructor(msg: String, cause: Exception) : super(msg, cause) {}
}
