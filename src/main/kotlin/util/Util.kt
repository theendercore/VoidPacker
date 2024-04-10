package com.theendercore.util

import com.theendercore.log
import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import java.io.File

fun Version(version:String): Version = version.toVersion()

fun dirValidator(){
    File("./").walk().forEach {
        log.info("*$it")
    }
}