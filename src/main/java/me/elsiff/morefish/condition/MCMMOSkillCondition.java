package me.elsiff.morefish.condition;

import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.hooker.MCMMOHooker;
import org.bukkit.entity.Player;

/**
 * Created by elsiff on 2017-06-20.
 */
public class MCMMOSkillCondition implements Condition {
    private String skillType;
    private int level;

    public MCMMOSkillCondition(String skillType, int level) {
        this.skillType = skillType;
        this.level = level;
    }

    @Override
    public boolean isSatisfying(Player player) {
        MCMMOHooker hooker = MoreFish.getInstance().getMCMMOHooker();

        if (hooker == null) {
            MoreFish.getInstance().getLogger().severe("MCMMO Not Found!");
            return false;
        }

        return (hooker.getSkillLevel(player, skillType) >= level);
    }
}
