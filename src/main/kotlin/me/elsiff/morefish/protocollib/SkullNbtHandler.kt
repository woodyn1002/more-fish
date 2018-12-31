package me.elsiff.morefish.protocollib

import com.comphenix.protocol.wrappers.nbt.NbtCompound
import com.comphenix.protocol.wrappers.nbt.NbtFactory
import org.bukkit.inventory.ItemStack
import java.util.*


/**
 * Created by elsiff on 2018-12-31.
 */
class SkullNbtHandler : ItemStackNbtHandler {
    fun writeTexture(itemStack: ItemStack, textureValue: String) {
        requireCraftItemStack(itemStack)

        val tag = NbtFactory.fromItemTag(itemStack) as NbtCompound
        val skullOwner = NbtFactory.ofCompound("SkullOwner")
        val properties = NbtFactory.ofCompound("Properties")

        val compound = NbtFactory.ofCompound("")
        compound.put("Value", textureValue)

        val textures = NbtFactory.ofList("textures", compound)
        properties.put(textures)
        skullOwner.put("Id", UUID.randomUUID().toString())
        skullOwner.put(properties)
        tag.put(skullOwner)

        NbtFactory.setItemTag(itemStack, tag)
    }
}