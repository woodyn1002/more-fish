package me.elsiff.morefish.fishing

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Created by elsiff on 2018-12-23.
 */
internal class FishTypeTableTest {

    @Test
    fun addType() {
        val fishTypes = FishTypeTable()
        val rarity = mock<FishRarity> {}
        val type = mock<FishType> {}

        assertFailsWith(IllegalStateException::class) {
            fishTypes.addType(type, rarity)
        }
    }

    @Test
    fun removeType() {
        val fishTypes = FishTypeTable()
        val rarity = mock<FishRarity> {}
        val type = mock<FishType> {}

        fishTypes.addRarity(rarity)

        assertFailsWith(IllegalStateException::class) {
            fishTypes.removeType(type, rarity)
        }
    }
}