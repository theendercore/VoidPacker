package com.theendercore.cmd

import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.log
import com.theendercore.util.DATA_PACKS
import com.theendercore.util.IMPORT
import com.theendercore.util.RESOURCE_PACKS
import com.theendercore.util.dirValidator
import java.io.File

class ImportCommand : CliktCommand(name = "import", help = "Imports a existing packs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        dirValidator()

        either {
            val imports = File(IMPORT).listFiles()
            ensureNotNull(imports) { Error("$IMPORT is empty or not created!") }

            imports.forEach loop@{
                if (it.isDirectory) {
                    val packMeta = it.resolve("pack.mcmeta")

                    val packInfo = if (packMeta.exists()) {
                        // process pack.mcmeta file
                    } else {
                        log.warn("pack.mcmeta not found in '${it.path}'")
                        return@loop
                    }

                    val dataFolder = it.resolve("data")
                    val assetsFolder = it.resolve("assets")
                    val newPack =
                        if (dataFolder.exists()) File("$DATA_PACKS/$it")
                        else if (assetsFolder.exists()) File("$RESOURCE_PACKS/$it")
                        else {
                            log.error("No 'data' or 'assets' folder found for '${it.path}'!")
                            return@loop
                        }
                    newPack.mkdirs()

                    if (dataFolder.exists()) dataFolder.copyRecursively(newPack)
                    if (assetsFolder.exists()) assetsFolder.copyRecursively(newPack)

                    val img = it.resolve("pack.png")
                    if (img.exists()) img.copyTo(newPack)
                    else log.warn("No pack png!")

                    log.info(it.path)
                } else if (it.isFile && it.endsWith(".zip")) {
                    log.info(it.name)
                }
            }
        }.leftOrNull()?.let { log.error(it.message) }
    }
}
