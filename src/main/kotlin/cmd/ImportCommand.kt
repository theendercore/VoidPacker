package com.theendercore.cmd

import arrow.core.Either
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
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.File
import java.io.FileOutputStream

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

                    ensure(!(isAFile("$DATA_PACKS/$name") || isAFile("$RESOURCE_PACKS/$name")))
                    { ImportWarn("Pack '$name' already exists. Skipping!") }
                    val unzipFolder = extractZipFile(it, name).bind()

                    import(unzipFolder, name)
                    unzipFolder.deleteRecursively()

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

fun extractZipFile(file: File, name: String): Either<ImportErrorType, File> = either {
    val outputFolder = File("$TEMP/$name")
    if (outputFolder.exists()) outputFolder.deleteRecursively()
    outputFolder.mkdirs()

    val zipFile = ZipFile.builder().setFile(file).get()
    try {
        zipFile.entries.asIterator().forEach { entry ->
            val outputFile = outputFolder.resolve(entry.name)
            if (!entry.isDirectory) {
                outputFile.parentFile.mkdirs()
                FileOutputStream(outputFile).use { output ->
                    zipFile.getInputStream(entry).use { it.copyTo(output) }
                }
            }

        }
    } catch (e: Exception) {
        ensure(false) { ImportError(e.message ?: "null") }
    }
    outputFolder
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
