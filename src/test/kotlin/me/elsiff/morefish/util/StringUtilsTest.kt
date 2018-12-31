package me.elsiff.morefish.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Created by elsiff on 2018-12-31.
 */
internal class StringUtilsTest {

    @Test
    fun format() {
        val formatted = StringUtils.format("I'm %placeholder%", mapOf(
                "%placeholder%" to "formatted"
        ))
        assertEquals("I'm formatted", formatted)
    }
}