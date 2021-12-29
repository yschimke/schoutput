package com.baulsupp.schoutput

import com.baulsupp.schoutput.handler.OutputHandler
import com.baulsupp.schoutput.responses.ResponseExtractor
import okio.Path

expect fun errPrintln(string: String?)

expect fun mimeType(path: Path): String?

expect fun <T> outputHandlerInstance(re: ResponseExtractor<T>): OutputHandler<T>
