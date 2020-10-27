package com.baulsupp.oksocial.output

suspend fun main() {
  val handler = ConsoleHandler.instance()

  handler.openLink("https://google.com")
}
