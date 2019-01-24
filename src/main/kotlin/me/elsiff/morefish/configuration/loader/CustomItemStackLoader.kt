package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.configuration.translated
import me.elsiff.morefish.hooker.PluginHooker
import me.elsiff.morefish.hooker.ProtocolLibHooker
import me.elsiff.morefish.item.edit
import me.elsiff.morefish.item.editIfHas
import me.elsiff.morefish.item.editIfIs
import me.elsiff.morefish.util.NamespacedKeyUtils
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Created by elsiff on 2019-01-09.
 */
class CustomItemStackLoader(
    private val enchantmentMapLoader: EnchantmentMapLoader
) : CustomLoader<ItemStack> {
    lateinit var protocolLib: ProtocolLibHooker

    override fun loadFrom(section: ConfigurationValueAccessor, path: String): ItemStack {
        section[path].let {
            val material = NamespacedKeyUtils.material(it.string("id"))
            val amount = it.int("amount", 1)
            var itemStack = ItemStack(material, amount)

            itemStack.edit<ItemMeta> {
                lore = it.strings("lore", emptyList()).translated()
                for ((enchantment, level) in enchantmentMapLoader.loadFrom(it, "enchantments")) {
                    addEnchant(enchantment, level, true)
                }
                isUnbreakable = it.boolean("unbreakable", false)
            }

            itemStack.editIfIs<Damageable> {
                damage = it.int("durability", 0)
            }

            if (it.contains("skull-uuid")) {
                itemStack.editIfHas<SkullMeta> {
                    val uuid = UUID.fromString(it.string("skull-uuid"))
                    owningPlayer = Bukkit.getOfflinePlayer(uuid)
                }
            }

            if (it.contains("skull-texture")) {
                PluginHooker.checkHooked(protocolLib)
                itemStack = protocolLib.skullNbtHandler.writeTexture(itemStack, it.string("skull-texture"))
            }
            return itemStack
        }
    }
}