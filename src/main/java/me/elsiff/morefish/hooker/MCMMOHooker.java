package me.elsiff.morefish.hooker;

import com.gmail.nossr50.api.ExperienceAPI;
import org.bukkit.entity.Player;

/**
 * Created by elsiff on 2017-06-20.
 */
public class MCMMOHooker {

    public int getSkillLevel(Player player, String skillType) {
        return ExperienceAPI.getLevel(player, skillType);
    }
}
