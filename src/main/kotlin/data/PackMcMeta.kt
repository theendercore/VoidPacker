package com.theendercore.data

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class PackMcMeta(val pack: Pack) {
    constructor(
        pack_format: Int,
        description: String,
        supported_formats: Pack.SupportedFormats? = null,
        pack_version: Version? = null,
    ) : this(Pack(pack_format, description, supported_formats, pack_version))

    @Serializable
    data class Pack(
        val pack_format: Int,
        val description: String,
        val supported_formats: SupportedFormats? = null,
        val pack_version: Version? = null,
    ) {
        @Serializable
        data class SupportedFormats(
            val min_inclusive: Int,
            val max_inclusive: Int
        )
    }
}

