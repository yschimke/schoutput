package com.baulsupp.oksocial.output.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.util.Map;

public class JsonUtil {
  public static final String JSON = "application/json";

  public static Map<String, Object> map(String content) throws IOException {
    ObjectMapper mapper = jsonMapper();
    return mapper.readValue(content, new TypeReference<Map<String, Object>>() {
    });
  }

  public static String toJson(Map<String, String> map) throws IOException {
    ObjectMapper mapper = jsonMapper();
    return mapper.writeValueAsString(map);
  }

  public static ObjectMapper jsonMapper() {
    return new ObjectMapper().registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

  public static ObjectMapper cborMapper() {
    return new ObjectMapper(new CBORFactory()).registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }
}
