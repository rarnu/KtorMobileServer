package com.rarnu.ktor.mobile

import java.io.OutputStream

class RedirectStream(val callback: (String) -> Unit): OutputStream() {

    override fun write(b: Int) {
        // do nothing
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        val s = String(b.dropLast(b.size - len).toByteArray()).trim()
        if (s != "") callback(s)
    }

}