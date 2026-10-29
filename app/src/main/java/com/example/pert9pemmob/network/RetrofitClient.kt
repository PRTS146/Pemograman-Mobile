package com.example.pert9pemmob.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton RetrofitClient managing OkHttpClient configuration,
 * logging interceptors, and Retrofit instance creation.
 */
object RetrofitClient {

    /**
     * Set [USE_LOCAL_EMULATOR] to true if testing with a local mock server running at http://10.0.2.2:8000/
     */
    const val USE_LOCAL_EMULATOR = false

    private const val PRODUCTION_BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/"

    val BASE_URL: String
        get() = if (USE_LOCAL_EMULATOR) EMULATOR_BASE_URL else PRODUCTION_BASE_URL

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
