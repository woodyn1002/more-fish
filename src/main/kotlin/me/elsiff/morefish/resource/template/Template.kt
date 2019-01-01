package me.elsiff.morefish.resource.template

/**
 * Created by elsiff on 2019-01-01.
 */
interface Template<T> {
    fun formatted(placeholders: Map<String, String>): T

    fun formattedEmpty(): T
}