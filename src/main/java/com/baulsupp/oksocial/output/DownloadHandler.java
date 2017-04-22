package com.baulsupp.oksocial.output;

import com.baulsupp.oksocial.output.util.OutputUtil;
import java.io.File;
import java.io.IOException;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

public class DownloadHandler<R> implements OutputHandler<R> {
  private ResponseExtractor<R> responseExtractor;

  private File outputFile;

  public DownloadHandler(ResponseExtractor<R> responseExtractor, File outputFile) {
    this.responseExtractor = responseExtractor;
    this.outputFile = outputFile;
  }

  @Override public void showOutput(R response) throws IOException {
    BufferedSource source = responseExtractor.source(response);

    Sink outputSink = getOutputSink(response);
    try {
      writeToSink(source, outputSink);
    } finally {
      if (!isStdout()) {
        outputSink.close();
      }
    }
  }

  public Sink getOutputSink(R response) throws IOException {
    if (isStdout()) {
      return OutputUtil.systemOut();
    } else if (outputFile.isDirectory()) {
      File responseOutputFile = new File(outputFile, responseExtractor.filename(response));
      System.err.println("Saving " + responseOutputFile);
      return Okio.sink(responseOutputFile);
    } else {
      if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
        if (!outputFile.getParentFile().mkdirs()) {
          throw new IOException("unable to create directory " + outputFile);
        }
      }
      return Okio.sink(outputFile);
    }
  }

  private boolean isStdout() {
    return outputFile.getPath().equals("-");
  }

  public static void writeToSink(BufferedSource source, Sink out) throws IOException {
    while (!source.exhausted()) {
      out.write(source.buffer(), source.buffer().size());
      out.flush();
    }
  }
}
