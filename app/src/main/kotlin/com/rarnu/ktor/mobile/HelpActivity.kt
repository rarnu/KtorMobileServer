package com.rarnu.ktor.mobile

import android.os.Bundle
import com.rarnu.android.BackActivity
import com.rarnu.android.resStr
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity: BackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        title = resStr(R.string.title_help)
        wvHelp.loadUrl("file:///android_asset/help.html")
    }
}