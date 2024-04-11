package com.theendercore.cmd

import arrow.core.raise.either
import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.config.Config
import com.theendercore.data.PackInfo
import com.theendercore.data.PackMcMeta
import com.theendercore.json
import com.theendercore.log
import com.theendercore.toml
import com.theendercore.util.*
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
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

        val metadata = mutableListOf<PublishingInfo>()
        val resourcePacks = File(RESOURCE_PACKS).listFiles()
        if (resourcePacks == null) {
            log.error("$RESOURCE_PACKS is empty or not created!")
            return
        }

        resourcePacks.forEach loop@{
            either {
                try {
                    val tempFolder = TEMP_EXPORT.resolve(it.name)

                    // data / assets folder
                    it.resolve("assets").copyRecursively(tempFolder.resolve("assets"))

                    // pack.png
                    val packPng = it.resolve("pack.png")
                    (if (packPng.exists()) packPng else File("./default.png"))
                        .copyTo(tempFolder.resolve("pack.png"))

                    // pack.mcmeta
                    val (packMeta, publishingInfo) = toml.decodeFromString<PackInfo>(it.resolve(PACK_INFO).readText())
                    val minVersion = dataVersionLookup(packMeta.minVersion)
                    val packMcMeta = PackMcMeta(
                        minVersion, packMeta.description,
                        if (packMeta.multiVersion)
                            PackMcMeta.Pack.SupportedFormats(minVersion, dataVersionLookup(Config.get().maxVersion))
                        else null
                    )
                    tempFolder.resolve("pack.mcmeta").writeText(json.encodeToString(packMcMeta))

                    // PublishingInfo
                    val processedInfo = PublishingInfo(
                        publishingInfo,
                        if (packMeta.multiVersion) PublishingInfo.VersionRange(
                            packMeta.minVersion,
                            Config.get().maxVersion
                        )
                        else PublishingInfo.VersionRange(packMeta.minVersion),
                        it.resolve(publishingInfo.changeLog).readText()
                    )
                    metadata.add(processedInfo)
                    metadata.add(processedInfo)

                    createZipFile(tempFolder, File("$EXPORT/${it.name}.zip"))


                } catch (e: Exception) {
                    raise(ErrorGenerate(e.toString()))
                }

            }.leftOrNull()?.let { log.error(it.msg) }
        }

        File("$EXPORT/publish.toml").writeText(toml.encodeToString(metadata))
        TEMP_EXPORT.deleteRecursively()
    }
}

sealed interface GenerateErrors

data class WarnGenerate(val msg: String) : GenerateErrors
data class ErrorGenerate(val msg: String) : GenerateErrors

@Serializable
data class PublishingInfo(
    val title: String,
    val projectId: String,
    val version: Version,
    val releaseType: PackInfo.PublishingInfo.ReleaseType,
    val mcVersion: VersionRange,
    val changelog: String? = null,
) {

    constructor(info: PackInfo.PublishingInfo, versionRange: VersionRange, changelog: String? = null) : this(
        info.name + " " + info.version,
        info.projectId,
        info.version,
        info.releaseType,
        versionRange,
        changelog
    )

    @Serializable
    data class VersionRange(val min: Version, val max: Version) {
        constructor(version: Version) : this(version, version)
    }
}

enum class PackType { DATA, RESOURCE }