package me.elsiff.morefish.item

import me.elsiff.morefish.fishing.Fish
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.tags.ItemTagType

/**
 * Created by elsiff on 2019-01-03.
 */
class FishItemTagWriter(
        private val fishTypeKey: NamespacedKey,
        private val fishLengthKey: NamespacedKey
) {
    fun write(itemMeta: ItemMeta, fish: Fish) {
        itemMeta.customTagContainer.run {
            setCustomTag(fishTypeKey, ItemTagType.STRING, fish.type.name)
            setCustomTag(fishLengthKey, ItemTagType.DOUBLE, fish.length)
        }
    }
}