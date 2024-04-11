package com.theendercore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.theendercore.cmd.DebugCommand
import com.theendercore.cmd.ExampleCommand
import com.theendercore.cmd.GenerateCommand
import com.theendercore.cmd.ImportCommand
import com.theendercore.config.Config
import kotlinx.serialization.json.Json
import net.peanuuutz.tomlkt.Toml
import net.peanuuutz.tomlkt.TomlIndentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("VoidPacker")
val toml = Toml {
    ignoreUnknownKeys = true
    indentation = TomlIndentation.Space4
}
val json = Json { prettyPrint = true }


class VoidPacker : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    Config.load()
    VoidPacker()
        .subcommands(GenerateCommand(), ExampleCommand(), ImportCommand(), DebugCommand())
        .main(args)
}

//enum class OperationType { GENERATE, PUBLISH, GENERATE_AND_PUBLISH, BUILD_ONE, GENERATE_EXAMPLE }