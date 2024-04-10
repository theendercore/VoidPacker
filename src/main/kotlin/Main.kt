package com.theendercore

import com.akuleshov7.ktoml.Toml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.theendercore.cmd.ExampleCommand
import com.theendercore.cmd.GenerateCommand
import com.theendercore.cmd.ImportCommand
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("VoidPacker")
val toml = Toml()
val json = Json { prettyPrint = true }


class VoidPacker : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) =
    VoidPacker()
        .subcommands(GenerateCommand(), ExampleCommand(), ImportCommand())
        .main(args)

//enum class OperationType { GENERATE, PUBLISH, GENERATE_AND_PUBLISH, BUILD_ONE, GENERATE_EXAMPLE }