package com.theendercore.data

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
data class MetadataHolder(val data: List<Metadata>)

@Serializable
data class Metadata(
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
