package com.theendercore.cmd

import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.config.Config
import com.theendercore.data.*
import com.theendercore.json
import com.theendercore.log
import com.theendercore.toml
import com.theendercore.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

class GenerateCommand : CliktCommand(name = "generate", help = "Generates the packs") {
//    val test: String by option().prompt("Your test").help("test value")

    private val TEMP_EXPORT = File("$TEMP/export")

    override fun run() {
        dirValidator()
        if (TEMP_EXPORT.exists()) TEMP_EXPORT.deleteRecursively()
        TEMP_EXPORT.mkdirs()
        val metadata = mutableListOf<Metadata>()

        val packList = mutableListOf<Pair<File, PackType>>()

        val resourcePacks = File(RESOURCE_PACKS).listFiles()
        val dataPacks = File(DATA_PACKS).listFiles()

        if (resourcePacks != null) packList.addAll(resourcePacks.map { it to PackType.RESOURCE })
        else log.warn("$RESOURCE_PACKS is empty or not created!")

        if (dataPacks != null) packList.addAll(dataPacks.map { it to PackType.DATA })
        else log.warn("$DATA_PACKS is empty or not created!")

        if (packList.isNotEmpty()) {
            packList.forEach { (it, type) ->
                either {
                    try {
                        val tempFolder = TEMP_EXPORT.resolve(it.name)
                        // data / assets folder
                        it.resolve(type.folder).copyRecursively(tempFolder.resolve(type.folder))

                        // pack.png
                        val packPng = it.resolve("pack.png")
                        (if (packPng.exists()) packPng else PACK_IMG)
                            .copyTo(tempFolder.resolve("pack.png"))

                        // pack.mcmeta
                        val (packMeta, publishingInfo) = toml.decodeFromString<PackInfo>(
                            it.resolve(PACK_INFO).readText()
                        )

                        val minVersion =
                            if (type.isData()) dataVersionLookup(packMeta.minVersion)
                            else resourceVersionLookup(packMeta.minVersion)
                        ensureNotNull(minVersion) { GenerateError("MinVersion was null for ${it.name}!") }

                        val maxVersion = if (type.isData()) dataVersionLookup(Config.get().maxVersion)
                        else resourceVersionLookup(Config.get().maxVersion)
                        ensureNotNull(maxVersion) { GenerateError("MaxVersion was null for ${it.name}!") }

                        val packMcMeta = PackMcMeta(
                            minVersion, packMeta.description,
                            if (packMeta.multiVersion)
                                PackMcMeta.Pack.SupportedFormats(minVersion, maxVersion)
                            else null
                        )

                        tempFolder.resolve("pack.mcmeta").writeText(json.encodeToString(packMcMeta))

                        // PublishingInfo
                        val processedInfo = Metadata(
                            publishingInfo,
                            if (packMeta.multiVersion) Metadata.VersionRange(
                                packMeta.minVersion,
                                Config.get().maxVersion
                            )
                            else Metadata.VersionRange(packMeta.minVersion),
                            it.resolve(publishingInfo.changeLog).readText()
                        )
                        metadata.add(processedInfo)

                        createZipFile(tempFolder, File("$EXPORT/${it.name}.zip"))

                        log.info("Finished ${it.name}!")

                    } catch (e: Exception) {
                        raise(GenerateError(e.toString()))
                    }

                }.leftOrNull()?.let { log.error(it.msg) }
            }
            METADATA_FILE.writeText(toml.encodeToString(MetadataHolder(metadata)))
        } else log.error("No packs where generated!")

        TEMP_EXPORT.deleteRecursively()
    }
}

sealed interface GenerateErrors

data class GenerateError(val msg: String) : GenerateErrors


