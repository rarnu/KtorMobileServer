package com.rarnu.ktor.mobile

import android.content.Context
import android.os.Environment
import dalvik.system.DexClassLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondBytes
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File

lateinit var appContext: Context

fun Application.module() {
    println("load module")
    routing {
        basicRoute()
        loadLibraries()
        this.children.forEach { println(it) }
    }
    println("Server started.")
}

fun Routing.basicRoute() {
    get("/") {
        println("call get(/)")
        call.respondBytes { WOK_HTML }
    }
}

fun Routing.loadLibraries() =
        File(Environment.getExternalStorageDirectory(), "KTOR").apply { if (!exists()) mkdirs() }
                .listFiles { _, name -> name.endsWith(".cfg") }.forEach { file ->
                    val m = file.readLines().filter { it.trim() != "" }.map { it.split("=").run { Pair(this[0], this[1]) } }.toMap()
                    loadLibrary(file, m["PluginClass"] ?: "", m["RoutingMethods"] ?: "")
                }


// 必须先把jar包制作成可供android识别的格式
// dx --dex --output=nCoVMapPlug-dex.jar nCoVMapPlug-1.0.0.jar
private fun Routing.loadLibrary(file: File, cls: String, method: String) {
    val fJar = File(file.absolutePath.substringBeforeLast(".") + ".jar")
    val fOut = File(Environment.getExternalStorageDirectory(), "KTOR").absolutePath
    if (fJar.exists() && cls != "" && method != "") {
        try {
            DexClassLoader(fJar.absolutePath, fOut, null, appContext.classLoader)
                    .loadClass(cls)
                    .getDeclaredMethod(method, Routing::class.java)
                    .invoke(null, this)
            println("load library: ${fJar.name}")
        } catch (th: Throwable) {
            println("load library ${fJar.name} failed, reason: $th")
        }
    }
}
