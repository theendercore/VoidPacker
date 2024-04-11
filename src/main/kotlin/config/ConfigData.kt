package com.theendercore.config

import com.theendercore.util.Version
import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(val maxVersion: Version) {
    constructor() : this(Version("1.20.4"))
}
