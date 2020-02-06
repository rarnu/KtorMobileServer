package com.rarnu.ktor.mobile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rarnu.android.resStr
import io.ktor.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class KtorService: Service() {

    companion object {
        private const val N_CHANNEL = "KtorServiceChannel"
        private const val N_NOTIFY_ID = 10010001
    }

    private var engine: ApplicationEngine? = null

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        goForeground()

        if (engine == null) {
            engine = embeddedServer(Netty, 8080, module = Application::module)
            engine?.start(false)
        }
    }

    override fun onDestroy() {
        if (engine != null) {
            thread {
                engine?.stop(1, 1, TimeUnit.SECONDS)
                engine = null
            }
        }
        stopForeground(true)
        super.onDestroy()
    }

    private fun goForeground() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(N_CHANNEL, N_CHANNEL, NotificationManager.IMPORTANCE_NONE)
            (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(channel)
            val notification = Notification.Builder(this, N_CHANNEL)
                    .setContentTitle(resStr(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_notify)
                    .build()
            startForeground(N_NOTIFY_ID, notification)
        }
    }


}