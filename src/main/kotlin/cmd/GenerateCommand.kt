package com.theendercore.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.log
import com.theendercore.util.dirValidator
import java.io.File

class GenerateCommand : CliktCommand(name = "generate", help = "Generates the packs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        dirValidator()
    }
}
