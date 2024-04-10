package org.example

import org.slf4j.LoggerFactory


fun main() {

    val log = LoggerFactory.getLogger("VoidPacker")
    val name = "Kotlin"
    println("Hello, $name!")

    log.info("Greetings!")
    for (i in 1..5) {
        println("i = $i")
    }
}