package com.theendercore.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.enum
import com.theendercore.data.PackInfo
import com.theendercore.toml
import com.theendercore.util.EXAMPLE
import com.theendercore.util.Version
import com.theendercore.util.dirValidator
import kotlinx.serialization.encodeToString
import java.io.File

class ExampleCommand : CliktCommand(name = "example", help = "Generates examples to use when running the program") {
    private val exampleType: ExampleType by option()
        .enum<ExampleType>()
        .prompt("Example Type ${ExampleType.entries}", default = ExampleType.PACK_TOML)
        .help("Type of the example to generate. Valid values - ${ExampleType.entries}")

    override fun run() {
        dirValidator()
        when (exampleType) {
            ExampleType.PACK_TOML -> {
                val packInfo = PackInfo(
                    PackInfo.PackMeta(
                        Version("1.20.1"),
                        "Test desc",
                    ),
                    PackInfo.PublishingInfo(
                        Version("1.0.0"),
                        "asas",
                        File("./test.md")
                    )
                )
                File("$EXAMPLE/example_pack.toml").writeText(toml.encodeToString(packInfo))
            }
        }
    }
}

enum class ExampleType { PACK_TOML }