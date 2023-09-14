package com.kiylx.libx.http.kotlin.basic2

import com.kiylx.libx.http.kotlin.common.BaseErrorHandler
import com.kiylx.libx.http.kotlin.common.RawResponse
import com.kiylx.libx.http.kotlin.common.RequestHandler
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call


suspend inline fun <reified T : Any> handleApi2(action: Call<T>): Resources2<T> {
    return handleApi2(action, null)
}

suspend inline infix fun <reified T : Any> CoroutineScope.handleApi2With(action: Call<T>): Resources2<T> {
    return handleApi2(action, null)
}

suspend inline fun <reified T : Any, reified E : BaseErrorHandler> handleApi2(
    action: Call<T>,
    errorHandler: E?,
): Resources2<T> {
    return when (val rawResponse: RawResponse<T> = RequestHandler.handle(action,errorHandler)) {
        is RawResponse.Error -> {
            Resources2.Error(rawResponse)
        }
        is RawResponse.Success -> {
            val info = rawResponse.responseData
            Resources2.Success(info)
        }
    }
}

