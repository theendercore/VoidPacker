package com.theendercore.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.log
import com.theendercore.toml
import com.theendercore.util.METADATA_FILE
import kotlinx.serialization.decodeFromString
import java.io.File

class DebugCommand : CliktCommand(name = "debug", help = "Debug command for all my debugging needs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {

        val x = toml.decodeFromString<MetadataHolder>(File(METADATA_FILE).readText()).toString()
        log.info(x)
    }
}
