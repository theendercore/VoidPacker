package com.theendercore.util

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import java.io.File

fun createZipFile(toZipFolder: File, outputFile: File) {
    ZipArchiveOutputStream(outputFile).use { archive ->
        toZipFolder.walk().forEach {
            val relativePath = it.toPath().toAbsolutePath().normalize().toString()
                .removePrefix(toZipFolder.toPath().toAbsolutePath().normalize().toString())
            if (it.isDirectory) {
                archive.putArchiveEntry(ZipArchiveEntry("$relativePath/"))
            } else {
                val entry = ZipArchiveEntry(it, relativePath)
                archive.putArchiveEntry(entry)
                it.inputStream().use { fis -> fis.copyTo(archive) }
            }
            archive.closeArchiveEntry()
        }
        archive.finish()
    }
}
