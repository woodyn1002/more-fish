package me.elsiff.morefish.resource.configuration

import me.elsiff.morefish.fishing.FishRarity
import me.elsiff.morefish.fishing.FishType
import me.elsiff.morefish.fishing.condition.Condition
import me.elsiff.morefish.item.edit
import me.elsiff.morefish.item.editIfHas
import me.elsiff.morefish.item.editIfIs
import me.elsiff.morefish.protocollib.SkullNbtHandler
import me.elsiff.morefish.resource.template.TextListTemplate
import me.elsiff.morefish.resource.template.TextTemplate
import me.elsiff.morefish.util.ColorUtils
import me.elsiff.morefish.util.NamespacedKeyUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Created by elsiff on 2018-12-28.
 */
fun ConfigurationSection.getTextTemplate(path: String): TextTemplate {
    return TextTemplate(getString(path))
}

fun ConfigurationSection.getTextListTemplate(path: String): TextListTemplate {
    return TextListTemplate(getStringList(path))
}

fun ConfigurationSection.getChildSections(path: String): List<ConfigurationSection> {
    val section = getConfigurationSection(path)
    return section.getKeys(false).map { section.getConfigurationSection(it) }
}

fun ConfigurationSection.getFishRarityList(path: String): List<FishRarity> {
    return getChildSections(path).map {
        FishRarity(
                name = it.name,
                displayName = ColorUtils.translate(it.getString("display-name")),
                default = it.getBoolean("default", false),
                probability = it.getDouble("chance", 0.0) / 100.0,
                color = ChatColor.valueOf(it.getString("color").toUpperCase()),
                feature = FishRarity.Feature(
                        additionalPrice = it.getDouble("additional-price", 0.0),
                        noBroadcast = it.getBoolean("no-broadcast", false),
                        noDisplay = it.getBoolean("no-display", false),
                        firework = it.getBoolean("firework", false)
                )
        )
    }
}

fun ConfigurationSection.getFishTypeList(
        path: String,
        rarity: FishRarity,
        skullNbtHandler: SkullNbtHandler?
): List<FishType> {
    return getConfigurationSection(path).getChildSections(rarity.name).map {
        FishType(
                name = it.name,
                displayName = ColorUtils.translate(it.getString("display-name")),
                rarity = rarity,
                lengthMin = it.getDouble("length-min"),
                lengthMax = it.getDouble("length-max"),
                icon = it.getFishItemStack("icon", skullNbtHandler),
                feature = FishType.Feature(
                        skipItemFormat = it.getBoolean("skip-item-format", false),
                        commands = it.getStringList("commands").map(ColorUtils::translate),
                        conditions = it.getStringList("conditions").map(Condition.Companion::valueOf)
                )
        )
    }
}

fun ConfigurationSection.getFishItemStack(path: String, skullNbtHandler: SkullNbtHandler?): ItemStack {
    val material = NamespacedKeyUtils.material(getString("$path.id"))
    val amount = getInt("$path.amount", 1)
    val itemStack = ItemStack(material, amount)

    itemStack.edit<ItemMeta> {
        lore = getStringList("$path.lore").map(ColorUtils::translate)
        getStringList("$path.enchantments").map {
            val tokens = it.split('|')
            Pair<Enchantment, Int>(Enchantment.getByKey(NamespacedKey.minecraft(tokens[0])), tokens[1].toInt())
        }.forEach {
            addEnchant(it.first, it.second, true)
        }
        isUnbreakable = getBoolean("$path.unbreakable", false)
    }

    itemStack.editIfIs<Damageable> {
        damage = getInt("$path.durability", 0)
    }

    itemStack.editIfHas<SkullMeta> {
        val uuid = UUID.fromString(getString("$path.skull-uuid"))
        owningPlayer = Bukkit.getOfflinePlayer(uuid)
    }

    skullNbtHandler?.writeTexture(itemStack, getString("$path.skull-texture"))

    return itemStack
}