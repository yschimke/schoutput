package com.baulsupp.oksocial.output

import okio.Path

actual fun errPrintln(string: String) = println(string)

actual fun mimeType(path: Path): String? = null