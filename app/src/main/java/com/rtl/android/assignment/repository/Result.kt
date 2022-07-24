package com.rtl.android.assignment.repository

sealed class Result<out T> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val type: Type) : Result<Nothing>() {
        enum class Type {
            API, NETWORK, DB
        }
    }
}