package me.elsiff.morefish.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IdentityUtils {

    public static Material getMaterial(String minecraftId) {
        try {
            Class<?> minecraftKey = Reflection.getNMSClass("MinecraftKey");
            Object mk = minecraftKey.getConstructor(String.class).newInstance(minecraftId);

            Class<?> craftItemStack = Reflection.getCBClass("inventory.CraftItemStack");
            Class<?> itemClass = Reflection.getNMSClass("Item");
            Class<?> registryMaterials = Reflection.getNMSClass("RegistryMaterials");

            Field field = itemClass.getDeclaredField("REGISTRY");
            field.setAccessible(true);
            Object registry = field.get(null);

            Method get = registryMaterials.getMethod("get", Object.class);
            Object item = get.invoke(registry, mk);

            Method asNewCraftStack = craftItemStack.getMethod("asNewCraftStack", itemClass);
            ItemStack itemStack = (ItemStack) asNewCraftStack.invoke(null, item);

            return itemStack.getType();
        } catch (Exception e) {
            return null;
        }
    }

    public static Enchantment getEnchantment(String minecraftId) {
        try {
            Class<?> minecraftKey = Reflection.getNMSClass("MinecraftKey");
            Object mk = minecraftKey.getConstructor(String.class).newInstance(minecraftId);

            Class<?> craftEnchantment = Reflection.getCBClass("enchantments.CraftEnchantment");
            Class<?> enchantmentClass = Reflection.getNMSClass("Enchantment");
            Class<?> registryMaterials = Reflection.getNMSClass("RegistryMaterials");

            Field field = enchantmentClass.getDeclaredField("enchantments");
            field.setAccessible(true);
            Object registry = field.get(null);

            Method get = registryMaterials.getMethod("get", Object.class);
            Object ench = get.invoke(registry, mk);

            for (Enchantment enchantment : Enchantment.values()) {
                Method getHandle = craftEnchantment.getMethod("getHandle");
                Object handle = getHandle.invoke(enchantment);

                if (ench.equals(handle)) {
                    return enchantment;
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
