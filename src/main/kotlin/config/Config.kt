package com.theendercore.config

import com.theendercore.log
import com.theendercore.toml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.FileWriter


object Config {
    private val configFile: File = File("./config.toml")

    private var config: ConfigData = ConfigData()
    fun get() = config
    fun load() {
        try {
            if (configFile.exists()) {
                config = toml.decodeFromString(configFile.readText())
            } else {
                log.info("No config found! Creating...")
                save(config)
            }
        } catch (e: Exception) {
            log.error("Could not load config! Making a new one.")
            log.error(e.toString())
            configFile.renameTo(File("./old_${configFile.name}"))
            save(config)
        }
    }

    private fun save(config: ConfigData) = FileWriter(configFile).use { it.write(toml.encodeToString(config)) }
}

