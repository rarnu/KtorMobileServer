package com.rarnu.ktor.mobile

import android.content.Context
import com.rarnu.android.assetsReadText

lateinit var WOK_HTML: ByteArray

fun Context.loadAssets() {
    WOK_HTML = assetsReadText("work.html").toByteArray()
}