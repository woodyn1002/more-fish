package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor

/**
 * Created by elsiff on 2019-01-09.
 */
interface CustomLoader<T> {
    fun loadFrom(section: ConfigurationValueAccessor, path: String = ROOT_PATH): T

    companion object {
        private const val ROOT_PATH = ""
    }
}