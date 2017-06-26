package com.baulsupp.oksocial.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Test;

import static java.util.Optional.of;

public class ConsoleHandlerTest {
  @Test
  public void testCborSupport() throws IOException {
    ObjectMapper cborMapper = new ObjectMapper(new CBORFactory());

    Map<String, String> s = new LinkedHashMap<String, String>();
    s.put("a", "AAA");
    s.put("b", "BBB");
    byte[] bytes = cborMapper.writeValueAsBytes(s);

    ResponseExtractor<byte[]> extractor = new ResponseExtractor<byte[]>() {
      @Override public Optional<String> mimeType(byte[] response) {
        return of("application/cbor");
      }

      @Override public BufferedSource source(byte[] response) throws IOException {
        return Okio.buffer(Okio.source(new ByteArrayInputStream(response)));
      }

      @Override public String filename(byte[] response) {
        return "filename";
      }
    };
    ConsoleHandler<byte[]> c = new ConsoleHandler<>(extractor);
    c.showOutput(bytes);
  }
}
