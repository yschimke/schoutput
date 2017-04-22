package com.baulsupp.oksocial.output.util;

import com.google.common.base.Throwables;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureUtil {
  public static <T> CompletableFuture<List<T>> join(List<CompletableFuture<T>> futures) {
    CompletableFuture[] cfs = futures.toArray(new CompletableFuture[futures.size()]);

    return CompletableFuture.allOf(cfs)
        .thenApply(v -> futures.stream().
            map(CompletableFuture::join).
            collect(Collectors.toList())
        );
  }

  public static <U> U ioSafeGet(Future<U> load) {
    try {
      return load.get();
    } catch (InterruptedException e) {
      throw Throwables.propagate(e);
    } catch (ExecutionException e) {
      throw Throwables.propagate(e.getCause());
    }
  }

  public static <T> Optional<T> or(Optional<T> option, Supplier<Optional<T>> callable) {
    if (option.isPresent()) {
      return option;
    } else {
      return callable.get();
    }
  }

  public static <T> CompletableFuture<T> failedFuture(Exception e) {
    CompletableFuture<T> f = new CompletableFuture<>();
    f.completeExceptionally(e);
    return f;
  }

  public static <T> Stream<T> optionalStream(Optional<T> ai) {
    return ai.map(Stream::of).orElseGet(Stream::empty);
  }
}
