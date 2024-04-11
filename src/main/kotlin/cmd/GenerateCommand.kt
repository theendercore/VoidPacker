package com.theendercore.cmd

import arrow.core.raise.either
import com.github.ajalt.clikt.core.CliktCommand
import com.theendercore.config.Config
import com.theendercore.data.PackInfo
import com.theendercore.data.PackMcMeta
import com.theendercore.json
import com.theendercore.log
import com.theendercore.toml
import com.theendercore.util.PACK_INFO
import com.theendercore.util.RESOURCE_PACKS
import com.theendercore.util.dataVersionLookup
import com.theendercore.util.dirValidator
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

class GenerateCommand : CliktCommand(name = "generate", help = "Generates the packs") {
//    val test: String by option().prompt("Your test").help("test value")

    override fun run() {
        dirValidator()
        val metadata = mutableListOf<PublishingInfo>()
        val resourcePacks = File(RESOURCE_PACKS).listFiles()
        if (resourcePacks == null) {
            log.error("$RESOURCE_PACKS is empty or not created!")
            return
        }

        val lost = listOf("hello", "hi", "good day")

        resourcePacks.forEach loop@{
            either {
                try {

                    // data / assets folder
//                    log.info(it.resolve("assets").toString())

                    // pack.png
                    val packPng = it.resolve("pack.png")
//                    log.info(if (packPng.exists()) packPng.toString() else "default.png")

                    // pack.mcmeta
                    val (packMeta, publishingInfo) = toml.decodeFromString<PackInfo>(it.resolve(PACK_INFO).readText())


                    val minVersion = dataVersionLookup(packMeta.minVersion)
                    val packMcMeta = PackMcMeta(
                        minVersion, packMeta.description,
                        if (packMeta.multiVersion)
                            PackMcMeta.Pack.SupportedFormats(minVersion, dataVersionLookup(Config.get().maxVersion))
                        else null
                    )
//                    log.info(json.encodeToString(packMcMeta))

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

//                    log.info(processedInfo.toString())

                } catch (e: Exception) {
                    raise(ErrorGenerate(e.toString()))
                }

            }.leftOrNull()?.let { log.error(it.msg) }
        }

        log.info(json.encodeToString(metadata))
        log.info(toml.encodeToString(metadata))

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