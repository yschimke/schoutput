package ee.schimke.oksocial.output.iterm;

import ee.schimke.oksocial.output.OsxOutputHandler;
import ee.schimke.oksocial.output.ResponseExtractor;
import java.io.IOException;

public class ItermOutputHandler<R> extends OsxOutputHandler<R> {
  public static final char ESC = (char) 27;
  public static final char BELL = (char) 7;

  public ItermOutputHandler(ResponseExtractor<R> responseExtractor) {
    super(responseExtractor);
  }

  // https://www.iterm2.com/documentation-images.html
  @Override public void openPreview(R response) throws IOException {
    String b64 = responseExtractor.source(response).readByteString().base64();

    System.out.print(ESC + "]1337;File=inline=1:");
    System.out.print(b64);
    System.out.print(BELL + "\n");
  }

  public static boolean isAvailable() {
    String term = System.getenv("TERM_PROGRAM");
    String version = System.getenv("TERM_PROGRAM_VERSION");
    return "iTerm.app".equals(term) && version != null && version.startsWith("3.");
  }
}
