package com.android.boilerplate.data.repositories.interceptor

import com.android.boilerplate.security.AuthEncryptedDataManager
import com.android.boilerplate.utils.CommonLogger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AccessTokenInterceptor(
    private val encryptedDataManager: AuthEncryptedDataManager = AuthEncryptedDataManager(),
    private val commonLogger: CommonLogger = CommonLogger.instance
) : Interceptor {

    companion object {
        private val TAG: String = AccessTokenInterceptor::class.java.simpleName
        var interceptorLogId = 0

        fun nextId(): Int {
            interceptorLogId++
            return interceptorLogId
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val generatedId = nextId()
        commonLogger.sysLog(TAG, "Start of Interception $generatedId")
        val request = newRequestWithAccessToken(chain.request())
        return chain.proceed(request)
    }

    private fun newRequestWithAccessToken(request: Request): Request {
        val requestBuilder = request.newBuilder()
        requestBuilder.header("Authorization", "Bearer ${encryptedDataManager.getAccessToken()}")
        return requestBuilder.build()
    }
}
