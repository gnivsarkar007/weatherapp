package com.rtl.android.assignment.util

import com.rtl.android.assignment.api.model.ApiResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object FileReader {
    @Throws(IOException::class)
    private fun readFileWithoutNewLineFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream =
                javaClass.classLoader?.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }

    fun apiResponse(): ApiResponse? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.getRawType(ApiResponse::class.java)
        val adapter: JsonAdapter<ApiResponse> = moshi.adapter(listType) as JsonAdapter<ApiResponse>
        val inputStream = readFileWithoutNewLineFromResources("sample.json")
        return adapter.fromJson(inputStream)
    }
}