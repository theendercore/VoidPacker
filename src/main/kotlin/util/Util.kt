package com.theendercore.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import java.io.File

fun Version(version: String): Version = version.toVersion()

fun formatName(name: String) = name
    .removeSuffix(".zip")
    .replace(Regex("(?<=[a-z])(?=[A-Z])|[_-]"), " ")

fun isAFile(path: String) = File(path).exists()