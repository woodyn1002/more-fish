package me.elsiff.morefish.condition;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCondition implements Condition {
    private Enchantment enchantment;
    private int level;

    public EnchantmentCondition(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public boolean isSatisfying(Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        return (hand != null && hand.containsEnchantment(enchantment) &&
                hand.getEnchantmentLevel(enchantment) >= level);
    }
}
