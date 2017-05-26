package me.elsiff.morefish.util;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkullUtils {
    private SkullUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static ItemStack setSkullTexture(ItemStack itemStack, String value) {
        ItemStack newStack = itemStack;
        if (!MinecraftReflection.isCraftItemStack(itemStack)) {
            newStack = MinecraftReflection.getBukkitItemStack(itemStack);
        }

        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(newStack);
        NbtCompound skullOwner = NbtFactory.ofCompound("SkullOwner");
        NbtCompound properties = NbtFactory.ofCompound("Properties");

        NbtCompound compound = NbtFactory.ofCompound("");
        compound.put("Value", value);

        NbtList<NbtCompound> textures = NbtFactory.ofList("textures", compound);
        properties.put(textures);
        skullOwner.put("Id", UUID.randomUUID().toString());
        skullOwner.put(properties);
        tag.put(skullOwner);

        NbtFactory.setItemTag(newStack, tag);
        return newStack;
    }
}
