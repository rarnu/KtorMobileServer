package com.rarnu.ktor.mobile

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.rarnu.android.resStr
import com.rarnu.android.runOnMainThread
import kotlinx.android.synthetic.main.activity_main.*
import java.io.PrintStream

class MainActivity : Activity() {

    companion object {
        private const val MENUID_HELP = Menu.FIRST + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        setContentView(R.layout.activity_main)
        loadAssets()

        // 重定向 print 输出
        System.setOut(PrintStream(RedirectStream { runOnMainThread { tvOutput.append("$it\n") } }))
        updateStatus()

        btnStart.setOnClickListener {
            if (isServiceRunning(KtorService::class.java.name)) {
                stopService(Intent(this, KtorService::class.java))
            } else {
                startForegroundService(Intent(this, KtorService::class.java))
            }
            updateStatus()
        }

        btnTest.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("http://0.0.0.0:8080") })
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENUID_HELP, 0, R.string.menu_help).apply {
            setIcon(android.R.drawable.ic_menu_help)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENUID_HELP -> startActivity(Intent(this, HelpActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    private fun Context.isServiceRunning(serviceName: String) = if (serviceName.trim() == "") false else (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(10).any { it.service.className == serviceName }

    private fun updateStatus() = if (isServiceRunning(KtorService::class.java.name)) {
        btnStart.setText(R.string.btn_stop)
        tvStatus.setText(R.string.text_server_started)
        tvStatus.setTextColor(Color.GREEN)
    } else {
        btnStart.setText(R.string.btn_start)
        tvStatus.setText(R.string.text_server_not_started)
        tvStatus.setTextColor(Color.LTGRAY)
    }

}


