package com.example.tramtracker.network.interceptor

import android.util.Log
import com.example.tramtracker.util.TAG
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class RetryInterceptor
@Inject
constructor() : Interceptor {
    companion object {
        private const val MAX_RETRIES: Int = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var requestOK = response.isSuccessful

        var tryCount = 0
        while (!requestOK && tryCount < MAX_RETRIES) {
            tryCount += 1
            Log.w(TAG, "Request not successful: $tryCount of $MAX_RETRIES")
            kotlin.runCatching { response.close() }
            Thread.sleep((tryCount * 1000).toLong())
            response = chain.proceed(request)
            requestOK = response.isSuccessful
        }

        if (!response.isSuccessful)
            throw IOException("+++TEST+++")

        return response
    }
}