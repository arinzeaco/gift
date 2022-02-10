package com.globasure.giftoga.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ApiUtil {
    companion object {
        fun createJsonRequestBody(vararg params: Pair<String, Any>) =
            JSONObject(mapOf(*params)).toString().toRequestBody("application/json; charset=utf-8".toMediaType())
    }
}