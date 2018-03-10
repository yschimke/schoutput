package com.baulsupp.oksocial.output.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.io.IOException

object JsonUtil {
  const val JSON = "application/json"

  @Throws(IOException::class)
  fun map(content: String): Map<String, Any> {
    val mapper = jsonMapper()
    return mapper.readValue(content, object : TypeReference<Map<String, Any>>() {
    })
  }

  @Throws(IOException::class)
  fun toJson(map: Map<String, String>): String {
    val mapper = jsonMapper()
    return mapper.writeValueAsString(map)
  }

  fun jsonMapper(): ObjectMapper {
    return ObjectMapper().registerModule(ParameterNamesModule())
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
  }

  fun cborMapper(): ObjectMapper {
    return ObjectMapper(CBORFactory()).registerModule(ParameterNamesModule())
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
  }
}
