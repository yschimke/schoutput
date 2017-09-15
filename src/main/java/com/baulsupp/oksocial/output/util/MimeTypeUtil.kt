package com.baulsupp.oksocial.output.util

import java.util.Optional
import javax.activation.MimeType
import javax.activation.MimeTypeParseException

object MimeTypeUtil {
  fun isMedia(mediaType: String): Boolean {
    return mediaType.startsWith("image/") || mediaType.endsWith("/pdf")
  }

  fun getExtension(mediaType: Optional<String>): String {
    if (mediaType.isPresent) {
      when (mediaType.get()) {
        "image/jpeg" -> return ".jpg"
        "image/gif" -> return ".gif"
        "image/png" -> return ".png"
        else -> return ".data"
      }
    }

    return ".data"
  }

  fun isCbor(mediaType: String): Boolean {
    return isMediaType(mediaType, "application/cbor") || mediaType
        .endsWith("+cbor")
  }

  fun isJson(mediaType: String): Boolean {
    return isMediaType(mediaType, "application/json", "text/json") || mediaType
        .endsWith("+json")
  }

  fun isMediaType(mediaType: String?, vararg types: String): Boolean {
    try {
      if (mediaType == null) {
        return false
      }

      val t = MimeType(mediaType)

      for (type in types) {
        if (t.match(type)) {
          return true
        }
      }

      return false
    } catch (e: MimeTypeParseException) {
      return false
    }

  }
}
