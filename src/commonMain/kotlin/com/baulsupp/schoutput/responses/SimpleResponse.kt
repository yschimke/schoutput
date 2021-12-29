package com.baulsupp.schoutput.responses

import okio.ByteString

data class SimpleResponse(val mimeType: String? = null, val source: ByteString = ByteString.EMPTY, val filename: String? = null)
