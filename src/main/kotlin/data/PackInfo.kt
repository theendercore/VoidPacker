package com.theendercore.data

import com.akuleshov7.ktoml.annotations.TomlComments
import com.theendercore.util.FileSerializer
import com.theendercore.util.Version
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class PackInfo(
    val packMeta: PackMeta,
    val publishingInfo: PublishingInfo
) {

    constructor(
        name: String, import: PackMetaImport, projectId: String = "null", version: Version = Version("1.0.0")
    ) : this(
        PackMeta(import.pack.pack_format, import.pack.description),
        PublishingInfo(name, import.pack.pack_version ?: version, projectId, File("changelog.md"))
    )

    @Serializable
    data class PackMeta(
        val minVersion: Int,
        val description: String,
        val multiVersion: Boolean = true
    )

    @Serializable
    data class PublishingInfo(
        val name: String,
        val version: Version,
        val projectId: String,
        @Serializable(with = FileSerializer::class) val changeLog: File, // Should be markdown imported from separate file or path to file
        @TomlComments("Possible values - [ALPHA, BETA, RELEASE]")
        val releaseType: ReleaseType = ReleaseType.RELEASE,
        val isMadeForSnapshots: Boolean = false,
    )
}

@Suppress("unused")
enum class ReleaseType { ALPHA, BETA, RELEASE }
