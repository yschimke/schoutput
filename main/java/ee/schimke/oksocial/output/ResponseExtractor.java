package ee.schimke.oksocial.output;

import java.util.Optional;
import okio.BufferedSource;

public interface ResponseExtractor<R> {
  Optional<String> mimeType(R response);

  BufferedSource source(R response);

  String filename(R response);
}
