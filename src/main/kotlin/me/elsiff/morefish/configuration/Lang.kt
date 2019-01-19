package me.elsiff.morefish.configuration

import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.configuration.format.TextListFormat
import java.time.Duration

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
        val builder = StringBuilder()
        val duration = Duration.ofSeconds(second)

        if (duration.toMinutes() > 0) {
            builder.append(duration.toMinutes())
                .append(text("time-format-minutes"))
                .append(" ")
        }
        builder.append(duration.seconds % 60)
            .append(text("time-format-seconds"))
        return builder.toString()
    }
}