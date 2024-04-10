package com.theendercore.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion

fun Version(version: String): Version = version.toVersion()
