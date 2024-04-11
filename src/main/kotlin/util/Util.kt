package com.theendercore.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion

fun Version(version: String): Version = version.toVersion()

fun formatName(name: String) = name
    .removeSuffix(".zip")
    .replace(Regex("(?<=[a-z])(?=[A-Z])|[_-]"), " ")