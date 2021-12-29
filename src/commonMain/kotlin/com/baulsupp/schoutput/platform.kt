package com.baulsupp.schoutput

import okio.Path

expect fun errPrintln(string: String): Unit

expect fun mimeType(path: Path): String?
