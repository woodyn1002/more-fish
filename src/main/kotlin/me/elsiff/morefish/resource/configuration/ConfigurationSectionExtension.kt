package me.elsiff.morefish.resource.configuration

import me.elsiff.morefish.item.edit
import me.elsiff.morefish.item.editIfHas
import me.elsiff.morefish.protocollib.ProtocolLibHooker
import me.elsiff.morefish.util.ColorUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Created by elsiff on 2018-12-28.
 */
fun ConfigurationSection.getCustomItemStack(path: String, protocolLib: ProtocolLibHooker): ItemStack {
    val itemStack = ItemStack(
            Material.matchMaterial(getString("$path.id")),
            getInt("$path.amount", 1),
            getInt("$path.durability", 0).toShort()
    )
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
    itemStack.editIfHas<SkullMeta> {
        val uuid = UUID.fromString(getString("$path.skull-uuid"))
        owningPlayer = Bukkit.getOfflinePlayer(uuid)
    }
    if (protocolLib.hasHooked && itemStack.itemMeta is SkullMeta) {
        protocolLib.skullNbtHandler.writeTexture(itemStack, getString("$path.skull-texture"))
    }
    return itemStack
}