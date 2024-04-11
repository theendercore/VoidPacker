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

fun List<Version>.isValid(version: Version) = (version in (this.first()..this.last()))

fun reversDataLookup(version: Int) = data[version]?.first()
fun dataVersionLookup(version: Version): Int {
    data.forEach {
        if (it.value.isValid(version)) return it.key

    }
    return 0
}
fun resourceVersionLookup(version: Version) =
    when (version) {
        Version("1.20.1") -> 15
        Version("1.20.2") -> 18
        Version("1.20.3") -> 22
        Version("1.20.4") -> 22
        else -> -1
    }
