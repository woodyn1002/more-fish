package me.elsiff.morefish.hooker

import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.nbt.NbtCompound
import com.comphenix.protocol.wrappers.nbt.NbtFactory
import org.bukkit.inventory.ItemStack
import java.util.*


/**
 * Created by elsiff on 2018-12-31.
 */
class SkullNbtHandler {
    fun writeTexture(itemStack: ItemStack, textureValue: String): ItemStack {
        val editingStack = if (MinecraftReflection.isCraftItemStack(itemStack))
            itemStack
        else
            MinecraftReflection.getBukkitItemStack(itemStack)

        val tag = NbtFactory.fromItemTag(editingStack) as NbtCompound
        val skullOwner = NbtFactory.ofCompound("SkullOwner")
        val properties = NbtFactory.ofCompound("Properties")

        val compound = NbtFactory.ofCompound("")
        compound.put("Value", textureValue)

        val textures = NbtFactory.ofList("textures", compound)
        properties.put(textures)
        skullOwner.put("Id", UUID.randomUUID().toString())
        skullOwner.put(properties)
        tag.put(skullOwner)

        NbtFactory.setItemTag(editingStack, tag)
        return editingStack
    }
}