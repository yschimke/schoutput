package com.baulsupp.oksocial.output.process

import com.baulsupp.oksocial.output.stdErrLogging
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.ProcessResult
import org.zeroturnaround.exec.listener.ProcessListener

data class ExecResult(val exitCode: Int, val output: ByteString?) {
  val success = exitCode == 0
  val outputString by lazy {
    output?.string(StandardCharsets.UTF_8)!!
  }
}

suspend fun exec(vararg command: String): ExecResult {
  return exec(command.toList()) {
    readOutput(true)
    redirectError(stdErrLogging)
    redirectInput(ByteArrayInputStream(byteArrayOf()))
  }
}

suspend fun exec(command: List<String>, configure: ProcessExecutor.() -> Unit = {}): ExecResult {
  var outputRequested = false

  val pe = object : ProcessExecutor() {
    override fun readOutput(readOutput: Boolean): ProcessExecutor {
      outputRequested = readOutput
      return super.readOutput(readOutput)
    }
  }

  pe.command(command.toList())

  configure(pe)

  return suspendCoroutine { cont ->
    pe.addListener(object : ProcessListener() {
      override fun afterFinish(process: Process, result: ProcessResult) {
        try {
          val outputString = if (outputRequested) {
            val output = result.output()
            output.toByteString()
          } else {
            null
          }

          cont.resume(ExecResult(result.exitValue, outputString))
        } catch (e: Exception) {
          cont.resumeWithException(e)
        }
      }
    })

    try {
      pe.start()
    } catch (e: Exception) {
      cont.resumeWithException(e)
    }
  }
}
