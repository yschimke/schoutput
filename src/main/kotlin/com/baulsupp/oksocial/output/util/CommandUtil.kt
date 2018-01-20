package com.baulsupp.oksocial.output.util

import org.zeroturnaround.exec.ProcessExecutor
import java.util.concurrent.ExecutionException

object CommandUtil {
  private fun isInstalledInternal(command: String): Boolean {
    val checkCommand = if (PlatformUtil.isOSX) arrayOf("command", "-v", command) else arrayOf("which", command)
    return ProcessExecutor().command(*checkCommand).execute().exitValue == 0
  }

  fun isInstalled(command: String): Boolean {
    return try {
      isInstalledInternal(command)
    } catch (e: ExecutionException) {
      e.cause!!.printStackTrace()
      false
    }
  }

  val isTerminal: Boolean
    get() = System.console() != null
}
