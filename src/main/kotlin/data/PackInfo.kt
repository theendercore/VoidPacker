package com.theendercore.data

import com.akuleshov7.ktoml.annotations.TomlComments
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

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
        val changeLog: String, // Should be markdown imported from separate file or path to file
        @TomlComments("Possible values - [ALPHA, BETA, RELEASE]")
        val releaseType: ReleaseType = ReleaseType.RELEASE,
        val isMadeForSnapshots: Boolean = false,
    )
}

enum class ReleaseType { ALPHA, BETA, RELEASE }
