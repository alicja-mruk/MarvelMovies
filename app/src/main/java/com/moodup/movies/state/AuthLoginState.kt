package com.moodup.movies.state

enum class AuthLoginState {
    ON_LOGIN_SUCCESS,
    ON_LOGIN_FAILURE,
    EMPTY_EMAIL_OR_PASSWORD_FIELD,
    ON_ALREADY_LOGGED_IN
}