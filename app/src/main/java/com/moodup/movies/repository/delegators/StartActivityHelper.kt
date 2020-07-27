package com.moodup.movies.repository.delegators

import android.app.Activity
import android.content.Context
import android.content.Intent

class StartActivityHelper (private val context: Context, private val className : Class<out Activity>  ) {

    fun startActivityWithClearTaskFlag() {
        val intent = Intent(context, className)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

