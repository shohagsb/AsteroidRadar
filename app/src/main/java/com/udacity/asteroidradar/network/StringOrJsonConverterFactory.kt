package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type


class StringOrJsonConverterFactory : Converter.Factory() {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        annotations.forEach { annotation ->
            return when (annotation) {
                is StringAnnotation -> ScalarsConverterFactory.create()
                    .responseBodyConverter(type, annotations, retrofit)
                is JsonAnnotation -> MoshiConverterFactory.create(moshi)
                    .responseBodyConverter(type, annotations, retrofit)
                else -> null
            }
        }
        return null
    }

    companion object {
        fun create() = StringOrJsonConverterFactory()
    }
}