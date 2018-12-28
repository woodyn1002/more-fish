package me.elsiff.morefish.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Created by elsiff on 2018-12-28.
 */
inline fun <reified T : ItemMeta> ItemStack.edit(block: T.() -> Unit) {
    itemMeta = (itemMeta as T).apply(block)
}

inline fun <reified T : ItemMeta> ItemStack.editIfHas(block: T.() -> Unit) {
    if (itemMeta is T) {
        itemMeta = (itemMeta as T).apply(block)
    }
}