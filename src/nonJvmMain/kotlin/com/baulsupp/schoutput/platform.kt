package com.baulsupp.schoutput

import okio.Path

actual fun errPrintln(string: String) = println(string)

actual fun mimeType(path: Path): String? = null
