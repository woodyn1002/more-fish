package me.elsiff.morefish.util

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

/**
 * Created by elsiff on 2018-12-31.
 */
internal class NamespacedKeyUtilsTest {

    @Test
    fun fromMinecraft() {
        assertEquals(NamespacedKey.minecraft("test"), NamespacedKeyUtils.fromMinecraft("minecraft:test"))
    }

    @Test
    fun material() {
        assertEquals(Material.COD, NamespacedKeyUtils.material("cod"))
        assertEquals(Material.COD, NamespacedKeyUtils.material("minecraft:cod"))
        assertFails { NamespacedKeyUtils.material("foo:bar") }
    }
}
