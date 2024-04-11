package com.theendercore.data

import kotlinx.serialization.Serializable
import io.github.z4kn4fein.semver.Version
@Suppress("PropertyName")
@Serializable
data class PackMetaImport(val pack: Pack) {
    @Serializable
    data class Pack(
        val pack_format: Int,
        val description: String,
        val supported_formats: SupportedFormats?,
        val pack_version: Version?,
    ) {
        @Serializable
        data class SupportedFormats(
            val min_inclusive: Int,
            val max_inclusive: Int
        )
    }
}

