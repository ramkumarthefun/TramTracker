package com.example.tramtracker.util

import android.os.Build

/*
    Tag for Log calls to easily see where the logs are being called from in logcat
 */

val Any.TAG: String
    get() {
        val name: String = if (javaClass.isAnonymousClass) javaClass.name else javaClass.simpleName
        return if (Build.VERSION.SDK_INT < 24 && name.length > 23) name.substring(0, 23) else name
    }