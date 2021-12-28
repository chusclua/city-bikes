package com.chus.clua.citybikes.presentation.utils.service

import com.chus.clua.citybikes.presentation.utils.mocks.RemoteModelGenerator.getJsonContentFromFileName
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

fun getRetrofit(baserUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baserUrl)
    .client(client())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun client(): OkHttpClient =
    OkHttpClient().newBuilder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .build()

@Throws(IOException::class)
fun enqueueMockResponseFromFile(fileName: String, responseCode: Int = 200): MockResponse  {
    return enqueueMockResponse(
        getJsonContentFromFileName(fileName),
        responseCode
    )
}

@Throws(IOException::class)
private fun enqueueMockResponse(responseBody: String, responseCode: Int): MockResponse {
    return MockResponse().apply {
        setResponseCode(responseCode)
        setBody(responseBody)
    }
}
