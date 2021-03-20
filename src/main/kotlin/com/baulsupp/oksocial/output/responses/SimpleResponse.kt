package com.baulsupp.oksocial.output.responses

import okio.ByteString

data class SimpleResponse(val mimeType: String? = null, val source: ByteString = ByteString.EMPTY, val filename: String? = null)
