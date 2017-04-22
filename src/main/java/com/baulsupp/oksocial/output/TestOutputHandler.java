package com.baulsupp.oksocial.output;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;

public class TestOutputHandler<R> implements OutputHandler<R> {
  public final List<R> responses = Lists.newArrayList();
  public final List<Throwable> failures = Lists.newArrayList();
  public final List<String> stdout = Lists.newArrayList();

  @Override public void showOutput(R response) throws IOException {
    responses.add(response);
  }

  @Override public void showError(String s, Throwable e) {
    failures.add(e);
  }

  @Override public void info(String s) {
    stdout.add(s);
  }
}
