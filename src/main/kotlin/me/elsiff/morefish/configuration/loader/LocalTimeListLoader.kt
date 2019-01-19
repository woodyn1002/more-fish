package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by elsiff on 2019-01-19.
 */
class LocalTimeListLoader : CustomLoader<List<LocalTime>> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun loadFrom(section: ConfigurationValueAccessor, path: String): List<LocalTime> {
        return section.strings(path).map { LocalTime.parse(it, formatter) }
    }
}