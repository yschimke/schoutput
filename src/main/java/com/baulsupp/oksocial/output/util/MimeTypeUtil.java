package com.baulsupp.oksocial.output.util;

import java.util.Optional;

public class MimeTypeUtil {
  public static boolean isMedia(String mediaType) {
    return mediaType.startsWith("image/") || mediaType.endsWith("/pdf");
  }

  public static String getExtension(Optional<String> mediaType) {
    if (mediaType.isPresent()) {
      switch (mediaType.get()) {
        case "image/jpeg":
          return ".jpg";
        case "image/gif":
          return ".gif";
        case "image/png":
          return ".png";
        default:
          return ".data";
      }
    }

    return ".data";
  }

  public static boolean isJson(String mediaType) {
    return isMediaType(mediaType, "application/cbor") || mediaType
        .endsWith("+cbor");
  }

  public static boolean isCbor(String mediaType) {
    return isMediaType(mediaType, "application/json", "text/json") || mediaType
        .endsWith("+json");
  }

  public static boolean isMediaType(String mediaType, String... types) {
    for (String type : types) {
      if (mediaType.equals(type)) {
        return true;
      }
    }

    return false;
  }
}
