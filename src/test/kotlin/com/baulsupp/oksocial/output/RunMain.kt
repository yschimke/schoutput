package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.handler.ConsoleHandler

suspend fun main() {
  val handler = ConsoleHandler.instance()

  handler.openLink("https://google.com")
}
