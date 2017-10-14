package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.util.OutputUtil
import okio.BufferedSource
import okio.Okio
import okio.Sink
import java.io.File
import java.io.IOException

class DownloadHandler<R>(private val responseExtractor: ResponseExtractor<R>, private val outputFile: File) : OutputHandler<R> {

    @Throws(IOException::class)
    override fun showOutput(response: R) {
        val source = responseExtractor.source(response)

        val outputSink = getOutputSink(response)
        try {
            writeToSink(source, outputSink)
        } finally {
            if (!isStdout) {
                outputSink.close()
            }
        }
    }

    @Throws(IOException::class)
    fun getOutputSink(response: R): Sink {
        return when {
            isStdout -> OutputUtil.systemOut()
            outputFile.isDirectory -> {
                val responseOutputFile = File(outputFile, responseExtractor.filename(response))
                System.err.println("Saving " + responseOutputFile)
                Okio.sink(responseOutputFile)
            }
            else -> {
                if (outputFile.parentFile != null && !outputFile.parentFile.exists()) {
                    if (!outputFile.parentFile.mkdirs()) {
                        throw IOException("unable to create directory " + outputFile)
                    }
                }
                Okio.sink(outputFile)
            }
        }
    }

    private val isStdout: Boolean
        get() = outputFile.path == "-"

    companion object {

        @Throws(IOException::class)
        fun writeToSink(source: BufferedSource, out: Sink) {
            while (!source.exhausted()) {
                out.write(source.buffer(), source.buffer().size())
                out.flush()
            }
        }
    }
}
