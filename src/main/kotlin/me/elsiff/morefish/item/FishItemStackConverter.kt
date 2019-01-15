package me.elsiff.morefish.item

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.ConfigurationSectionAccessor
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishTypeTable
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin

/**
 * Created by elsiff on 2018-12-28.
 */
class FishItemStackConverter(
    plugin: Plugin,
    fishTypeTable: FishTypeTable
) {
    private val fishReader: FishItemTagReader
    private val fishWriter: FishItemTagWriter

    private val formatConfig: ConfigurationSectionAccessor
        get() = Config.fish["item-format"]

    init {
        val fishTypeKey = NamespacedKey(plugin, "fishType")
        val fishLengthKey = NamespacedKey(plugin, "fishLength")
        fishReader = FishItemTagReader(fishTypeTable, fishTypeKey, fishLengthKey)
        fishWriter = FishItemTagWriter(fishTypeKey, fishLengthKey)
    }

    fun isFish(itemStack: ItemStack): Boolean {
        return fishReader.canRead(itemStack.itemMeta)
    }

    fun fish(itemStack: ItemStack): Fish {
        return fishReader.read(itemStack.itemMeta)
    }

    fun createItemStack(fish: Fish, catcher: Player): ItemStack {
        val itemStack = fish.type.icon.clone()
        if (!fish.type.hasNotFishItemFormat) {
            val replacement = getFormatReplacementMap(fish, catcher)
            itemStack.edit<ItemMeta> {
                displayName = formatConfig.format("display-name").replace(replacement).output
                lore = formatConfig.formats("lore").replace(replacement).output
                fishWriter.write(this, fish)
            }
        }
        return itemStack
    }

    private fun getFormatReplacementMap(fish: Fish, catcher: Player): Map<String, String> {
        return mapOf(
            "%player%" to catcher.name,
            "%rarity%" to fish.type.rarity.name.toUpperCase(),
            "%rarity_color%" to fish.type.rarity.color.toString(),
            "%length%" to fish.length.toString(),
            "%fish%" to fish.type.displayName
        )
    }
}