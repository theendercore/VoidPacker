package com.theendercore

import com.akuleshov7.ktoml.Toml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.theendercore.cmd.ExampleCommand
import com.theendercore.cmd.GenerateCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("VoidPacker")
val toml = Toml()


class VoidPacker : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) =
    VoidPacker()
        .subcommands(GenerateCommand(), ExampleCommand())
        .main(args)

//enum class OperationType { GENERATE, PUBLISH, GENERATE_AND_PUBLISH, BUILD_ONE, GENERATE_EXAMPLE }