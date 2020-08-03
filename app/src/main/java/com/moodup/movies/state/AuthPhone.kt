package com.moodup.movies.state

enum class AuthPhone {
    INITIALIZED,
    SMS_RECEIVED,
    WRONG_CONFIRMATION_CODE,
    INVALID_REQUEST,
    TOO_MANY_REQUESTS,
    LOGIN_SUCCESS
}