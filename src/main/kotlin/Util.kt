package com.theendercore

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion

fun Version(version:String): Version = version.toVersion()