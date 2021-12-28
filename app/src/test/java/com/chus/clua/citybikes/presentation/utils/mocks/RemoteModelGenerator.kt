package com.chus.clua.citybikes.presentation.utils.mocks

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.nio.charset.Charset


object RemoteModelGenerator {
    inline fun <reified T> getEntityFromFile(fileName: String): T {
        val jsonString =
            getJsonContentFromFileName(
                fileName
            )
        return gsonBuilder()
            .fromJson(jsonString, T::class.java)
    }

    fun getJsonContentFromFileName(fileName: String) =
        getStringContentFromFile(
            getFile(
                fileName
            )
        )

    private fun getStringContentFromFile(file: File): String {
        val inputStream = file.inputStream()
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }

    private fun getFile(fileName: String): File {
        val resourceUrl = RemoteModelGenerator::class.java.classLoader?.getResource(fileName)
            ?: throw RuntimeException("Couldn't get the classLoader")
        return File(resourceUrl.path)
    }

    fun gsonBuilder(): Gson =
        GsonBuilder().setLenient().serializeNulls().registerTypeAdapterFactory(
            AlwaysListTypAdapterFactory()
        ).create()

}

