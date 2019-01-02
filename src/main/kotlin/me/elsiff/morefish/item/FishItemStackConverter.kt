package me.elsiff.morefish.item

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.protocollib.FishNbtHandler
import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import me.elsiff.morefish.resource.configuration.getTextListTemplate
import me.elsiff.morefish.resource.configuration.getTextTemplate
import me.elsiff.morefish.resource.template.TextListTemplate
import me.elsiff.morefish.resource.template.TextTemplate
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by elsiff on 2018-12-28.
 */
class FishItemStackConverter(
        private val fishTypes: FishTypeTable
) : ResourceReceiver {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")!!
    private lateinit var displayNameFormat: TextTemplate
    private lateinit var loreFormat: TextListTemplate
    private var fishNbtHandler: FishNbtHandler? = null

    override fun receiveResource(resources: ResourceBundle) {
        resources.fish.getConfigurationSection("item-format").let {
            displayNameFormat = it.getTextTemplate("display-name")
            loreFormat = it.getTextListTemplate("lore")
        }
        fishNbtHandler = with(resources.protocolLib) {
            if (hasHooked) fishNbtHandler else null
        }
    }

    fun fish(itemStack: ItemStack): Fish {
        return fishNbtHandler?.readFishData(itemStack, fishTypes)
                ?: throw IllegalStateException("Couldn't find Fish Nbt Handler")
    }

    fun createItemStack(fish: Fish, catcher: Player, caughtDate: LocalDateTime): ItemStack {
        val itemStack = fish.type.icon.clone()
        val replacement = getFormatReplacementMap(fish, catcher, caughtDate)
        itemStack.edit<ItemMeta> {
            displayName = displayNameFormat.formatted(replacement)
            lore = loreFormat.formatted(replacement)
        }
        fishNbtHandler?.writeFishData(itemStack, fish)
        return itemStack
    }

    private fun getFormatReplacementMap(fish: Fish, catcher: Player, date: LocalDateTime): Map<String, String> {
        return mapOf(
                "%player%" to catcher.name,
                "%rarity%" to fish.type.rarity.name.toUpperCase(),
                "%rarity_color%" to fish.type.rarity.color.toString(),
                "%length%" to fish.length.toString(),
                "%fish%" to fish.type.displayName,
                "%date%" to dateTimeFormatter.format(date)
        )
    }
}