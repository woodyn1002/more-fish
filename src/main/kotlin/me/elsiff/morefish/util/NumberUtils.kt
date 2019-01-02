package me.elsiff.morefish.util

/**
 * Created by elsiff on 2019-01-02.
 */
object NumberUtils {
    fun getOrdinalSuffix(number: Int): String {
        if (number % 100 !in 11..13) {
            when (number % 10) {
                1 -> return "st"
                2 -> return "nd"
                3 -> return "rd"
            }
        }
        return "th"
    }

    fun getOrdinal(number: Int): String = "$number${getOrdinalSuffix(number)}"
}