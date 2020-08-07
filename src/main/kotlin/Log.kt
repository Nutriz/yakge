import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Log {

    private val dateTime: String?
        get() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    fun debug(text: Any?) =            println("$dateTime: DEBUG - $text")
    fun info(text:  Any?) =            println("$dateTime: INFO - $text")
    fun error(text: Any?) = System.err.println("$dateTime: ERROR - $text")
}