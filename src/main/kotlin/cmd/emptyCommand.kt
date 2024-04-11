package com.theendercore.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.util.dirValidator

@Suppress("ClassName", "unused")
class emptyCommand : CliktCommand(name = "", help = "") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        dirValidator()
    }
}
