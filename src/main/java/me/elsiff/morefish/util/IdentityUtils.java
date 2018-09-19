package me.elsiff.morefish.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

public class IdentityUtils {
    private IdentityUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static Material getMaterial(String minecraftId) {
        return Material.matchMaterial(minecraftId);
    }

    public static Enchantment getEnchantment(String minecraftId) {
        final NamespacedKey key = NamespacedKey.minecraft(getNameWithoutNamespace(minecraftId));
        return Enchantment.getByKey(key);
    }

    public static PotionEffectType getPotionEffectType(String minecraftId) {
        return PotionEffectType.getByName(getNameWithoutNamespace(minecraftId));
    }

    private static String getNameWithoutNamespace(final String minecraftId) {
        return minecraftId.contains(":") ? minecraftId.split(":")[1] : minecraftId;
    }
}
