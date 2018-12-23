package me.elsiff.morefish.fishing

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalStateException
import kotlin.test.assertFailsWith

/**
 * Created by elsiff on 2018-12-23.
 */
internal class FishTypeFactoryTest {

    @Test
    fun addType() {
        val factory = FishTypeFactory()
        val rarity = mock<FishRarity> {}
        val type = mock<FishType> {}

        assertFailsWith(IllegalStateException::class) {
            factory.addType(type, rarity)
        }
    }

    @Test
    fun removeType() {
        val factory = FishTypeFactory()
        val rarity = mock<FishRarity> {}
        val type = mock<FishType> {}

        factory.addRarity(rarity)

        assertFailsWith(IllegalStateException::class) {
            factory.removeType(type, rarity)
        }
    }
}