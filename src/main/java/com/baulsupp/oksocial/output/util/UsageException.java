package com.baulsupp.oksocial.output.util;

public class UsageException extends RuntimeException {
  public UsageException(String msg) {
    super(msg);
  }

  public UsageException(String msg, Exception cause) {
    super(msg, cause);
  }
}
