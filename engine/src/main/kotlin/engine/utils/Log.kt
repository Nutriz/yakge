package engine.utils

import engine.utils.Log.LogLevel.Debug
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Log {

    enum class LogLevel {Info, Debug, Error}

    var logLevel = LogLevel.Info

    private val dateTime: String
        get() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    private val currentThread: String
        get() = Thread.currentThread().name

    fun info(text:  Any?) {
        if (logLevel != Debug) {
            println("$dateTime: [INFO][$currentThread] - $text")
        }
    }
    fun debug(text: Any?) =            println("$dateTime: [DEBUG][$currentThread] - $text")
    fun error(text: Any?) = System.err.println("$dateTime: [ERROR][$currentThread] - $text")
}