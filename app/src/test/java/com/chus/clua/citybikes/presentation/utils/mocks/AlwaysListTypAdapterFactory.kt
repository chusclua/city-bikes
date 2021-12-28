package com.chus.clua.citybikes.presentation.utils.mocks

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.google.gson.stream.MalformedJsonException
import org.json.JSONObject.NULL
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AlwaysListTypAdapterFactory : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson?, typeToken: TypeToken<T>?): TypeAdapter<T>? {
        // If it's not a List -- just delegate the job to Gson and let it pick the best type adapter itself
        typeToken?.let {
            if (!MutableList::class.java.isAssignableFrom(it.rawType)) {
                return null
            }
            // Resolving the list parameter type
            val elementType: Type = resolveTypeArgument(it.type)
            val elementTypeAdapter = gson?.getAdapter(TypeToken.get(elementType)) as TypeAdapter<T>
            // Note that the always-list type adapter is made null-safe, so we don't have to check nulls ourselves
            return AlwaysListTypeAdapter(elementTypeAdapter).nullSafe() as TypeAdapter<T>
        } ?: run { return null }
    }

    private fun resolveTypeArgument(type: Type): Type {
        // The given type is not parameterized?
        if (type !is ParameterizedType) {
            // No, raw
            return Any::class.java
        }
        return type.actualTypeArguments[0]
    }

    inner class AlwaysListTypeAdapter<E>(private val elementTypeAdapter: TypeAdapter<E>) : TypeAdapter<List<E>>() {

        override fun write(out: JsonWriter?, value: List<E>) {
            throw UnsupportedOperationException()
        }

        override fun read(`in`: JsonReader?): List<E> {
            // This is where we detect the list "type"
            val list: MutableList<E> = ArrayList()
            `in`?.let {
                when (val token: JsonToken = it.peek()) {
                    JsonToken.BEGIN_ARRAY -> {
                        // If it's a regular list, just consume [, <all elements>, and ]
                        it.beginArray()
                        while (it.hasNext()) {
                            list.add(elementTypeAdapter.read(it))
                        }
                        it.endArray()
                    }
                    JsonToken.BEGIN_OBJECT, JsonToken.STRING, JsonToken.NUMBER, JsonToken.BOOLEAN ->
                        // An object or a primitive? Just add the current value to the result list
                        list.add(elementTypeAdapter.read(it))
                    NULL -> throw AssertionError("Must never happen: check if the type adapter configured with .nullSafe()")
                    JsonToken.END_ARRAY, JsonToken.END_OBJECT, JsonToken.END_DOCUMENT -> throw MalformedJsonException("Unexpected token: $token")
                    else -> throw AssertionError("Must never happen: $token")
                }
            }
            return list
        }
    }

}
