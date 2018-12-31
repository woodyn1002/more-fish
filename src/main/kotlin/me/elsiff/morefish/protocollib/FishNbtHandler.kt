package me.elsiff.morefish.protocollib

import com.comphenix.protocol.wrappers.nbt.NbtCompound
import com.comphenix.protocol.wrappers.nbt.NbtFactory
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishTypeTable
import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2018-12-31.
 */
class FishNbtHandler(
        private val fishTypes: FishTypeTable
) : ItemStackNbtHandler {
    fun writeFishData(itemStack: ItemStack, fish: Fish) {
        requireCraftItemStack(itemStack)

        val tag = NbtFactory.fromItemTag(itemStack) as NbtCompound
        val fishData = NbtFactory.ofCompound("fishData")
        val fishType = NbtFactory.of("type", fish.type.name)
        val length = NbtFactory.of("length", fish.length)

        fishData.put(fishType)
        fishData.put(length)
        tag.put(fishData)

        NbtFactory.setItemTag(itemStack, tag)
    }

    fun readFishData(itemStack: ItemStack): Fish {
        requireCraftItemStack(itemStack)

        val tag = NbtFactory.fromItemTag(itemStack) as NbtCompound
        val fishData = tag.getCompound("fishData")

        val rarity = fishTypes.getType(fishData.getString("type"))
        val length = fishData.getDouble("length")
        return Fish(rarity, length)
    }
}