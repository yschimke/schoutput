package com.baulsupp.oksocial.output;

import com.baulsupp.oksocial.output.handler.ConsoleHandler;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JavaClientTest {
  @Test
  public void testPreviewFileFail() {
    assertThrows(FileNotFoundException.class,
      () -> ConsoleHandler.previewFile(new File("sdfjkhfd.txt")));
  }

  @Test
  public void testPreviewFileWorks() {
    ConsoleHandler.previewFile(new File("src/test/resources/PNG_transparency_demonstration_1.png"));
  }
}
