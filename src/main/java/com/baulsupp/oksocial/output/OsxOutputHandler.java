package com.baulsupp.oksocial.output;

import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Optional.of;

public class OsxOutputHandler<R> extends ConsoleHandler<R> {
  public OsxOutputHandler(ResponseExtractor<R> responseExtractor) {
    super(responseExtractor);
  }

  public void openPreview(R response) throws IOException {
    streamToCommand(of(responseExtractor.source(response)),
        asList("open", "-f", "-a", "/Applications/Preview.app"), of(30));
  }
}
