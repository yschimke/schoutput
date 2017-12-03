package com.baulsupp.oksocial.output.util

import javax.activation.MimeType
import javax.activation.MimeTypeParseException

object MimeTypeUtil {
  fun isMedia(mediaType: String): Boolean {
    return mediaType.startsWith("image/") || mediaType.endsWith("/pdf")
  }

  fun getExtension(mediaType: String?): String = when (mediaType) {
    "image/jpeg" -> ".jpg"
    "image/gif" -> ".gif"
    "image/png" -> ".png"
    else -> ".data"
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
    return try {
      if (mediaType == null) {
        return false
      }

      val t = MimeType(mediaType)

      types.any { t.match(it) }
    } catch (e: MimeTypeParseException) {
      false
    }
  }
}
