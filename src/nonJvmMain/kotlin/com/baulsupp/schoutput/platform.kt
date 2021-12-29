package com.baulsupp.schoutput

import com.baulsupp.schoutput.handler.ConsoleHandler
import com.baulsupp.schoutput.handler.OutputHandler
import com.baulsupp.schoutput.responses.ResponseExtractor
import okio.Path

actual fun errPrintln(string: String?) = println(string)

actual fun mimeType(path: Path): String? = null

actual fun <T> outputHandlerInstance(re: ResponseExtractor<T>): OutputHandler<T> = ConsoleHandler(re)
