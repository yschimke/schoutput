package com.baulsupp.oksocial.output.util

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import org.zeroturnaround.exec.ProcessExecutor
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException

object CommandUtil {
  private val installed = CacheBuilder.newBuilder().build(object : CacheLoader<String, Boolean>() {
    @Throws(Exception::class)
    override fun load(command: String): Boolean? {
      return isInstalledInternal(command)
    }
  })

  @Throws(InterruptedException::class, TimeoutException::class, IOException::class)
  private fun isInstalledInternal(command: String): Boolean {
    return ProcessExecutor().command("command", "-v", command).execute().exitValue == 0
  }

  fun isInstalled(command: String): Boolean {
    return try {
      installed.get(command)
    } catch (e: ExecutionException) {
      e.cause!!.printStackTrace()
      false
    }

  }

  val isTerminal: Boolean
    get() = System.console() != null
}
