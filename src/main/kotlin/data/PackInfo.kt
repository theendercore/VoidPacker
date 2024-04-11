package com.theendercore.data

import com.theendercore.util.CHANGELOG
import com.theendercore.util.FileSerializer
import com.theendercore.util.Version
import com.theendercore.util.reversDataLookup
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
import net.peanuuutz.tomlkt.TomlComment
import java.io.File

@Serializable
data class PackInfo(
    val packMeta: PackMeta,
    val publishingInfo: PublishingInfo
) {
    constructor(
        name: String, import: PackMcMeta, projectId: String = "null", version: Version = Version("1.0.0"),
        changeLog: File =  File("./$CHANGELOG")
    ) : this(
        PackMeta(reversDataLookup(import.pack.pack_format) ?: Version("0.0.0"), import.pack.description),
        PublishingInfo(name, import.pack.pack_version ?: version, projectId, changeLog)
    )

    @Serializable
    data class PackMeta(
        val minVersion: Version,
        val description: String,
        val multiVersion: Boolean = true
    )

    @Serializable
    data class PublishingInfo(
        val name: String,
        val version: Version,
        val projectId: String,
        @Serializable(with = FileSerializer::class) val changeLog: File,
        @TomlComment("Possible values - [ALPHA, BETA, RELEASE]")
        val releaseType: ReleaseType = ReleaseType.RELEASE,
        val isMadeForSnapshots: Boolean = false,
    ) {
        @Suppress("unused")
        enum class ReleaseType { ALPHA, BETA, RELEASE }
    }
}

