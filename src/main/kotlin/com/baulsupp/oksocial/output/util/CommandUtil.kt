package com.baulsupp.oksocial.output.util

import com.baulsupp.oksocial.output.process.exec

object CommandUtil {
  suspend fun isInstalled(command: String): Boolean = if (PlatformUtil.isOSX) {
    exec("command", "-v", command).success
  } else {
    exec("which", command).success
  }

  val isTerminal: Boolean
    get() = System.console() != null
}
