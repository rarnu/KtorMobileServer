package com.rarnu.ktor.mobile

import android.util.Log
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.module() {
    Log.e("KTOR", "MODULE")
    println("")
    routing {
        helloRoute()
    }
}

fun Routing.helloRoute() {
    get("/hello") {
        Log.e("KTOR", "HELLO")
        call.respondText { """{"result":0}""" }
    }
}