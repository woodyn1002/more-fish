package me.elsiff.morefish.item

import me.elsiff.morefish.fishing.Fish
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/**
 * Created by elsiff on 2019-01-03.
 */
class FishItemTagWriter(
    private val fishTypeKey: NamespacedKey,
    private val fishLengthKey: NamespacedKey
) {
    fun write(itemMeta: ItemMeta, fish: Fish) {
        itemMeta.persistentDataContainer.run {
            set(fishTypeKey, PersistentDataType.STRING, fish.type.name)
            set(fishLengthKey, PersistentDataType.DOUBLE, fish.length)
        }
    }
}
