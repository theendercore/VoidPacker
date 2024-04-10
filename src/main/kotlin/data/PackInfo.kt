package com.theendercore.data

import com.akuleshov7.ktoml.annotations.TomlComments
import com.theendercore.util.FileSerializer
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class PackInfo(
    val packMeta: PackMeta,
    val publishingInfo: PublishingInfo
) {

    @Serializable
    data class PackMeta(
        val minVersion: Version,
        val description: String,
    )

    @Serializable
    data class PublishingInfo(
        val version: Version,
        val projectId: String,
        @Serializable(with = FileSerializer::class) val changeLog: File, // Should be markdown imported from separate file or path to file
        @TomlComments("Possible values - [ALPHA, BETA, RELEASE]")
        val releaseType: ReleaseType = ReleaseType.RELEASE,
        val isMadeForSnapshots: Boolean = false,
    )
}

enum class ReleaseType { ALPHA, BETA, RELEASE }
