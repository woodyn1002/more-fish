package me.elsiff.morefish.hooker

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.elsiff.morefish.MoreFish
import org.bukkit.Location

/**
 * Created by elsiff on 2019-01-20.
 */
class WorldGuardHooker : PluginHooker {
    override val pluginName = "WorldGuard"
    override var hasHooked: Boolean = false

    override fun hook(plugin: MoreFish) {
        hasHooked = true
    }

    fun containsLocation(regionId: String, location: Location): Boolean {
        val bukkitWorld: org.bukkit.World = location.world ?: return false
        val world = BukkitAdapter.adapt(bukkitWorld)
        val regions = WorldGuard.getInstance().platform.regionContainer.get(world) ?: return false
        return regions.getRegion(regionId)?.contains(location.blockX, location.blockY, location.blockZ) ?: false
    }
}
