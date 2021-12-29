package com.baulsupp.schoutput

import com.baulsupp.schoutput.handler.*
import com.baulsupp.schoutput.handler.iterm.ItermOutputHandler
import com.baulsupp.schoutput.handler.iterm.ItermOutputHandler.Companion.itermIsAvailable
import com.baulsupp.schoutput.responses.ResponseExtractor
import okio.Path
import java.nio.file.Files

actual fun errPrintln(string: String?) = System.err.println(string)

actual fun mimeType(path: Path) = Files.probeContentType(path.toNioPath())

actual fun <T> outputHandlerInstance(re: ResponseExtractor<T>): OutputHandler<T> {
  return when {
    itermIsAvailable() -> ItermOutputHandler(re)
    isOSX -> OsxOutputHandler(re)
    isLinux -> LinuxOutputHandler(re)
    isWindows -> WindowsOutputHandler(re)
    else -> ConsoleHandler(re)
  }
}
