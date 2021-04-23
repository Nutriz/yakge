package engine.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Log {

    private val dateTime: String
        get() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    private val currentThread: String
        get() = Thread.currentThread().name

    fun debug(text: Any?) =            println("$dateTime: [DEBUG][$currentThread] - $text")
    fun info(text:  Any?) =            println("$dateTime: [INFO][$currentThread] - $text")
    fun error(text: Any?) = System.err.println("$dateTime: [ERROR][$currentThread] - $text")
}