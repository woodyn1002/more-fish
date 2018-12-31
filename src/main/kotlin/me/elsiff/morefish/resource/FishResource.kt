package me.elsiff.morefish.resource

import me.elsiff.morefish.fishing.FishRarity
import me.elsiff.morefish.fishing.FishType
import me.elsiff.morefish.fishing.condition.Condition
import me.elsiff.morefish.protocollib.ProtocolLibHooker
import me.elsiff.morefish.resource.configuration.FileConfigurationHandler
import me.elsiff.morefish.resource.configuration.getCustomItemStack
import me.elsiff.morefish.util.ColorUtils
import org.bukkit.ChatColor

/**
 * Created by elsiff on 2018-12-28.
 */
class FishResource : FileConfigurationHandler() {
    inner class ItemFormat : Section(this, "item-format") {
        val displayName by StringValue("display-name")
        val lore by StringListValue("lore")
    }

    inner class RarityList : Value<List<FishRarity>>("rarity-list", { cfg, path ->
        cfg.getConfigurationSection(path).childSections().map {
            FishRarity(
                    name = it.name,
                    displayName = ColorUtils.translate(it.getString("display-name")),
                    default = it.getBoolean("default", false),
                    chance = it.getDouble("chance", 0.0),
                    color = ChatColor.valueOf(it.getString("color")),
                    feature = FishRarity.Feature(
                            additionalPrice = it.getDouble("additional-price", 0.0),
                            noBroadcast = it.getBoolean("no-broadcast", false),
                            noDisplay = it.getBoolean("no-display", false),
                            firework = it.getBoolean("firework", false)
                    )
            )
        }
    })

    inner class FishList : Section(this, "fish-list") {
        fun getFromRarity(rarity: FishRarity, protocolLib: ProtocolLibHooker): List<FishType> {
            return childSections(rarity.name).map {
                FishType(
                        name = it.name,
                        displayName = ColorUtils.translate(it.getString("display-name")),
                        rarity = rarity,
                        lengthMin = it.getDouble("length-min"),
                        lengthMax = it.getDouble("length-max"),
                        icon = it.getCustomItemStack("icon", protocolLib),
                        feature = FishType.Feature(
                                skipItemFormat = it.getBoolean("skip-item-format", false),
                                commands = it.getStringList("commands").map(ColorUtils::translate),
                                conditions = it.getStringList("conditions").map(Condition.Companion::valueOf)
                        )
                )
            }
        }
    }

    val version by IntValue("version")
    val itemFormat = ItemFormat()
    val rarityList = RarityList()
    val fishList = FishList()
}