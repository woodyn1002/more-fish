package me.elsiff.morefish.configuration

import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.configuration.format.TextListFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

/**
 * Created by elsiff on 2019-01-09.
 */
object Lang {
    const val ALTERNATE_COLOR_CODE = '&'
    private val langConfig: ConfigurationAccessor = Config.lang

    fun text(id: String): String =
        langConfig.string(id).translated()

    fun texts(id: String): List<String> =
        langConfig.strings(id).translated()

    fun format(id: String): TextFormat =
        TextFormat(langConfig.string(id))

    fun formats(id: String): TextListFormat =
        TextListFormat(langConfig.strings(id))

    fun time(second: Long): String {
        val builder = DateTimeFormatterBuilder()
        if (second / 60 > 0) {
            builder.appendValue(ChronoField.MINUTE_OF_HOUR)
                .appendLiteral(text("time-format-minutes"))
                .appendLiteral(" ")
        }
        builder.appendValue(ChronoField.SECOND_OF_MINUTE)
            .appendLiteral(text("time-format-seconds"))
        return LocalTime.ofSecondOfDay(second).format(builder.toFormatter())
    }
}