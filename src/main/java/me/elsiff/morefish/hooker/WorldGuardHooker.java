package me.elsiff.morefish.hooker;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

/**
 * Created by elsiff on 2017-06-20.
 */
public class WorldGuardHooker {

    public boolean containsLocation(Location loc, String regionId) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        ProtectedRegion region = WGBukkit.getRegionManager(loc.getWorld()).getRegion(regionId);

        return (region != null && region.contains(x, y, z));
    }
}
