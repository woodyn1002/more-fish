package me.elsiff.morefish.util

import org.bukkit.Material
import org.bukkit.NamespacedKey

/**
 * Created by elsiff on 2018-12-31.
 */
object NamespacedKeyUtils {
    fun fromMinecraft(id: String): NamespacedKey {
        if (id.contains(':'))
            return fromMinecraft(id.split(':')[1])
        return NamespacedKey.minecraft(id)
    }

    fun material(id: String): Material = material(fromMinecraft(id))

    fun material(namespacedKey: NamespacedKey): Material {
        return Material.matchMaterial(namespacedKey.key)
            ?: throw IllegalStateException("There's no material whose id is '$namespacedKey'")
    }
}