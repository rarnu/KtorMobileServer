package com.rarnu.ktor.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.rarnu.common.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            startForegroundService(Intent(this, KtorService::class.java))
        }

        btnStop.setOnClickListener {
            stopService(Intent(this, KtorService::class.java))
        }

        btnTest.setOnClickListener {
            thread {
                val str = httpGet("http://0.0.0.0:8080/hello")
                Log.e("KTOR", "resp: $str")
            }
        }
    }

}


