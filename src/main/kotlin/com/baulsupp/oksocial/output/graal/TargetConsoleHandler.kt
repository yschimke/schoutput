package com.baulsupp.oksocial.output.graal

import com.baulsupp.oksocial.output.ConsoleHandler
import com.baulsupp.oksocial.output.UsageException
import com.baulsupp.oksocial.output.exec
import com.baulsupp.oksocial.output.execResult
import com.baulsupp.oksocial.output.isLinux
import com.baulsupp.oksocial.output.isOSX
import com.baulsupp.oksocial.output.isWindows
import com.oracle.svm.core.annotate.Substitute
import com.oracle.svm.core.annotate.TargetClass

@TargetClass(ConsoleHandler::class)
class TargetConsoleHandler {
  @Substitute
  suspend fun openLink(url: String) {
    if (isOSX) {
      val result = execResult("open", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

      if (result != 0) {
        throw UsageException("open url failed: $url")
      }
    } else if (isLinux) {
      val result = execResult("xdg-open", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

      if (result != 0) {
        throw UsageException("open url failed: $url")
      }
    } else if (isWindows) {
      val result = execResult("rundll32", "url.dll,FileProtocolHandler", url, outputMode = ConsoleHandler.Companion.OutputMode.Hide)

      if (result != 0) {
        throw UsageException("open url failed: $url")
      }
    } else {
      System.err.println(url)
    }
  }
}
