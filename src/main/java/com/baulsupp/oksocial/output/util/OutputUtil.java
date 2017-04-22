package com.baulsupp.oksocial.output.util;

import okio.Okio;
import okio.Sink;

public class OutputUtil {

  public static Sink systemOut() {
    return Okio.sink(System.out);
  }
}
