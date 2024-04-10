package com.theendercore.util

import com.theendercore.log
import java.nio.file.Files
import java.nio.file.Paths


const val DATA_PACKS = "./data_packs"
const val RESOURCE_PACKS = "./resource_packs"
const val EXPORT = "./export"
const val IMPORT = "./import"
const val EXAMPLE = "./example"

fun dirValidator() {
    listOf(DATA_PACKS, RESOURCE_PACKS, EXPORT, IMPORT, EXAMPLE)
        .forEach {
            val directoryPath = Paths.get(it)
            if (!Files.exists(directoryPath)) {
                log.warn("'$it' folder missing! Creating a new one!")
                Files.createDirectories(directoryPath)
            }
        }

    log.info("Folders validated!")
}
