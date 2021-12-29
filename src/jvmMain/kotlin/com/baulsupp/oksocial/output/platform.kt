package com.baulsupp.oksocial.output

import okio.Path
import java.nio.file.Files

actual fun errPrintln(string: String) = System.err.println(string)

actual fun mimeType(path: Path) = Files.probeContentType(path.toNioPath())