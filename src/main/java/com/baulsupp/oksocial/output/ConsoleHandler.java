package com.baulsupp.oksocial.output;

import com.baulsupp.oksocial.output.iterm.ItermOutputHandler;
import com.baulsupp.oksocial.output.util.CommandUtil;
import com.baulsupp.oksocial.output.util.OutputUtil;
import com.baulsupp.oksocial.output.util.PlatformUtil;
import com.baulsupp.oksocial.output.util.MimeTypeUtil;
import com.baulsupp.oksocial.output.util.UsageException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import static com.baulsupp.oksocial.output.util.MimeTypeUtil.isJson;
import static com.baulsupp.oksocial.output.util.MimeTypeUtil.isMedia;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class ConsoleHandler<R> implements OutputHandler<R> {
  private static Logger logger = Logger.getLogger(ConsoleHandler.class.getName());
  protected ResponseExtractor<R> responseExtractor;

  public ConsoleHandler(ResponseExtractor<R> responseExtractor) {
    this.responseExtractor = responseExtractor;
  }

  @Override public void showError(String message, Throwable e) {
    if (logger.isLoggable(Level.FINE)) {
      logger.log(Level.WARNING, message, e);
    }

    if (e instanceof UsageException) {
      System.err.println(e.getMessage());
    } else if (e instanceof UnknownHostException && e.getCause() == null) {
      if (message != null) {
        System.err.print(message + ": ");
      }
      System.err.println(e.toString());
    } else {
      if (message != null) {
        System.err.println(message);
      }
      if (e != null) {
        e.printStackTrace();
      }
    }
  }

  @Override public void showOutput(R response) throws IOException {
    Optional<String> mimeType = responseExtractor.mimeType(response);

    if (mimeType.isPresent()) {
      if (isMedia(mimeType.get())) {
        openPreview(response);
        return;
      } else if (CommandUtil.isInstalled("jq") && (isJson(mimeType.get()))) {
        prettyPrintJson(response);
        return;
      }
    }

    // TODO support a nice hex mode for binary files
    DownloadHandler.writeToSink(responseExtractor.source(response), OutputUtil.systemOut());
    System.out.println("");
  }

  private void prettyPrintJson(R response) throws IOException {
    List<String> command = CommandUtil.isTerminal() ? asList("jq", "-C", ".") : asList("jq", ".");
    streamToCommand(Optional.of(responseExtractor.source(response)), command, Optional.empty());
  }

  public void streamToCommand(Optional<BufferedSource> source, List<String> command,
      Optional<Integer> timeout)
      throws IOException {
    try {
      ProcessExecutor pe = new ProcessExecutor().command(command)
          .redirectOutput(System.out)
          .redirectError(Slf4jStream.ofCaller().asInfo());

      timeout.ifPresent(integer -> pe.timeout(integer, TimeUnit.SECONDS));

      source.ifPresent(bufferedSource -> pe.redirectInput(bufferedSource.inputStream()));

      ProcessResult pr = pe.execute();

      if (pr.getExitValue() != 0) {
        throw new IOException(
            "return code " + pr.getExitValue() + " from " + command.stream().collect(joining(" ")));
      }
    } catch (InterruptedException | TimeoutException e) {
      throw new IOException(e);
    }
  }

  public void openPreview(R response) throws IOException {
    if (Desktop.isDesktopSupported()) {
      File tempFile = writeToFile(response);

      Desktop.getDesktop().open(tempFile);
    } else {
      System.err.println("Falling back to console output, use -r to avoid warning");

      DownloadHandler.writeToSink(responseExtractor.source(response), OutputUtil.systemOut());
      System.out.println("");
    }
  }

  public File writeToFile(R response) throws IOException {
    Optional<String> mimeType = responseExtractor.mimeType(response);

    File tempFile =
        File.createTempFile("output", MimeTypeUtil.getExtension(mimeType));

    try (Sink out = Okio.sink(tempFile)) {
      DownloadHandler.writeToSink(responseExtractor.source(response), out);
    }
    return tempFile;
  }

  @Override public void openLink(String url) throws IOException {
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(URI.create(url));
    } else {
      System.err.println(url);
    }
  }

  public int terminalWidth() throws IOException {
    try {
      ProcessExecutor pe = new ProcessExecutor().command("/bin/stty", "-a", "-f", "/dev/tty")
          .timeout(5, TimeUnit.SECONDS)
          .redirectError(Slf4jStream.ofCaller().asInfo())
          .readOutput(true);

      String output = pe.execute().outputString();

      Pattern p = Pattern.compile("(\\d+) columns", Pattern.MULTILINE);

      Matcher m = p.matcher(output);
      if (m.find()) {
        return Integer.parseInt(m.group(1));
      } else {
        return 80;
      }
    } catch (InterruptedException ie) {
      throw new InterruptedIOException(ie.getMessage());
    } catch (TimeoutException e) {
      throw new IOException(e);
    }
  }

  public static ConsoleHandler instance(ResponseExtractor re) {
    if (ItermOutputHandler.isAvailable()) {
      return new ItermOutputHandler(re);
    } else if (PlatformUtil.isOSX()) {
      return new OsxOutputHandler(re);
    } else {
      return new ConsoleHandler(re);
    }
  }
}
