package com.theendercore.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.log
import com.theendercore.util.formatName

class DebugCommand : CliktCommand(name = "debug", help = "Debug command for all my debugging needs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        listOf("HwelooThere", "Ho are You :)", "Cool_Pack_25", "test-pack-4" , "TEST HERE")
            .forEach { log.info(formatName(it)) }
    }
}
