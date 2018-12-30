package me.elsiff.morefish.item

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.util.StringUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by elsiff on 2018-12-28.
 */
class FishItemStackConverter(
        private val resources: ResourceBundle
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")!!

    fun fish(itemStack: ItemStack): Fish {
        return TODO("Not implemented")
    }

    fun createItemStack(fish: Fish, catcher: Player, caughtDate: LocalDateTime): ItemStack {
        val itemStack = fish.type.icon.clone()
        val replacement = getFormatReplacementMap(fish, catcher, caughtDate)
        resources.fish.itemFormat.let { itemFormat ->
            itemStack.edit<ItemMeta> {
                displayName = StringUtils.format(itemFormat.displayName, replacement)
                lore = itemFormat.lore.map { StringUtils.format(it, replacement) }
            }
        }
        return itemStack
    }

    private fun getFormatReplacementMap(fish: Fish, catcher: Player, date: LocalDateTime): Map<String, String> {
        return mapOf(
                "%player%" to catcher.name,
                "%rarity%" to fish.type.rarity.name.toUpperCase(),
                "%raritycolor%" to fish.type.rarity.color.toString(),
                "%fish%" to fish.type.displayName,
                "%date%" to dateTimeFormatter.format(date)
        )
    }
}