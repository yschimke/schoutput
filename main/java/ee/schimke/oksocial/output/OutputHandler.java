package ee.schimke.oksocial.output;

import java.io.IOException;

public interface OutputHandler<R> {
  void showOutput(R response) throws IOException;

  default void showError(String message, Throwable e) {
    if (message != null) {
      System.err.println(message);
    }
    if (e != null) {
      e.printStackTrace();
    }
  }

  default void openLink(String url) throws IOException {
    System.err.println(url);
  }

  default void info(String message) {
    System.out.println(message);
  }
}
