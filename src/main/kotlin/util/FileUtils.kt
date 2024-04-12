package com.theendercore.util

import com.theendercore.log
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

const val PACK_INFO = "pack.toml"
const val CHANGELOG = "changelog.md"


const val DATA_PACKS = "./data_packs"
const val RESOURCE_PACKS = "./resource_packs"
const val EXPORT = "./export"
const val IMPORT = "./import"
const val EXAMPLE = "./example"
const val DEFAULTS = "./defaults"
const val TEMP = "./temp"

val METADATA_FILE = File("$EXPORT/metadata.toml")
val PACK_IMG = File("$DEFAULTS/pack.png")


fun dirValidator() = listOf(DATA_PACKS, RESOURCE_PACKS, EXPORT, IMPORT, EXAMPLE, TEMP, DEFAULTS)
    .forEach {
        val directoryPath = Paths.get(it)
        if (!Files.exists(directoryPath)) {
            log.warn("'$it' folder missing! Creating a new one!")
            Files.createDirectories(directoryPath)
        }
    }
