package me.elsiff.morefish.configuration.format

/**
 * Created by elsiff on 2019-01-09.
 */
interface Format<T, R> {
    val output: R

    fun replace(vararg pairs: Pair<String, Any>): T

    fun replace(pairs: Map<String, Any>): T =
        replace(*pairs.toList().toTypedArray())
}