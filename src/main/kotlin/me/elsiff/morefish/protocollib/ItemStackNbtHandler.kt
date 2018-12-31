package me.elsiff.morefish.protocollib

import com.comphenix.protocol.utility.MinecraftReflection
import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2018-12-31.
 */
interface ItemStackNbtHandler {
    fun requireCraftItemStack(itemStack: ItemStack) {
        require(MinecraftReflection.isCraftItemStack(itemStack)) { "Item stack must be a CraftItemStack instance" }
    }
}