package com.baulsupp.oksocial.output.util

import okio.Okio
import okio.Sink

object OutputUtil {

  fun systemOut(): Sink {
    return Okio.sink(System.out)
  }
}
