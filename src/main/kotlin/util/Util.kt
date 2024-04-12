package com.theendercore.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import java.io.File

fun Version(version: String): Version = version.toVersion()

fun formatName(name: String) = name
    .removeSuffix(".zip")
    .replace(Regex("(?<=[a-z])(?=[A-Z])|[_-]"), " ")

fun isAFile(path: String) = File(path).exists()
val data = mapOf(
    15 to listOf(Version("1.20.1")),
    18 to listOf(Version("1.20.2")),
    26 to listOf(Version("1.20.3"), Version("1.20.4"))
)

val resource = mapOf(
    15 to listOf(Version("1.20.1")),
    18 to listOf(Version("1.20.2")),
    22 to listOf(Version("1.20.3"), Version("1.20.4"))
)

fun List<Version>.isValid(version: Version) = (version in (this.first()..this.last()))

fun reversDataLookup(version: Int) = data[version]?.first()
fun revereResourceLookup(version: Int) = resource[version]?.first()
fun dataVersionLookup(version: Version): Int? = data.entries.find { it.value.isValid(version) }?.key
fun resourceVersionLookup(version: Version): Int? = resource.entries.find { it.value.isValid(version) }?.key
