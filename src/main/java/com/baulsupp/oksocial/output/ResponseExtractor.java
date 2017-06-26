package com.baulsupp.oksocial.output;

import java.io.IOException;
import java.util.Optional;
import okio.BufferedSource;

public interface ResponseExtractor<R> {
  Optional<String> mimeType(R response);

  BufferedSource source(R response) throws IOException;

  String filename(R response);
}
