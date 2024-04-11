package com.theendercore.cmd

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.data.PackInfo
import com.theendercore.data.PackMetaImport
import com.theendercore.json
import com.theendercore.log
import com.theendercore.toml
import com.theendercore.util.*
import kotlinx.serialization.encodeToString
import java.io.File

class ImportCommand : CliktCommand(name = "import", help = "Imports a existing packs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        dirValidator()

        val imports = File(IMPORT).listFiles()
        if (imports == null) {
            log.error("$IMPORT is empty or not created!")
            return
        }

        imports.forEach loop@{
            either {
                if (it.isDirectory) import(it, it.name).bind()
                else if (it.isFile && it.name.endsWith(".zip")) {
                    val name = it.name.removeSuffix(".zip")

                    log.info(it.toString())


                }
            }.leftOrNull()?.let {
                when (it) {
                    is ImportWarn -> log.warn(it.msg)
                    is ImportError -> log.error(it.msg)
                }
            }
        }
    }
}

fun import(packFolder: File, name: String) = either {
    val dataFolder = packFolder.resolve("data")
    val assetsFolder = packFolder.resolve("assets")

    ensure(dataFolder.exists() || assetsFolder.exists())
    { ImportError("No 'data' or 'assets' folder found for '$name'!") }
    val newPack =
        if (dataFolder.exists()) File("$DATA_PACKS/$name")
        else File("$RESOURCE_PACKS/$name")

    ensure(!newPack.exists()) { ImportWarn("Pack '$name' already exists. Skipping!") }

    newPack.mkdirs()

    val packMeta = packFolder.resolve("pack.mcmeta")
    ensure(packMeta.exists()) { ImportError("pack.mcmeta not found in '$name'") }
    val packInfo =
        PackInfo(formatName(name), json.decodeFromString<PackMetaImport>(packMeta.readText()))


    if (dataFolder.exists()) dataFolder.copyRecursively(newPack.resolve("data"))
    if (assetsFolder.exists()) assetsFolder.copyRecursively(newPack.resolve("assets"))

    newPack.resolve("pack.toml").writeText(toml.encodeToString(packInfo))
    newPack.resolve("changelog.md").createNewFile()

    val img = packFolder.resolve("pack.png")
    if (img.exists()) img.copyTo(newPack.resolve("pack.png"))
    else log.warn("No pack png!")


    log.info("Pack '$name' imported!")

}


sealed interface ImportErrorType

data class ImportWarn(val msg: String) : ImportErrorType
data class ImportError(val msg: String) : ImportErrorType
