package me.elsiff.morefish.hooker

import org.bukkit.Location
import org.bukkit.Server
import com.sk89q.worldguard.bukkit.WGBukkit


/**
 * Created by elsiff on 2019-01-20.
 */
class WorldGuardHooker : PluginHooker {
    override val pluginName = "WorldGuard"
    override var hasHooked: Boolean = false

    override fun hook(server: Server) {
        hasHooked = true
    }

    fun containsLocation(regionId: String, location: Location): Boolean {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        val region = WGBukkit.getRegionManager(location.world).getRegion(regionId)
            ?: throw IllegalStateException("Region '$regionId' doesn't exist")

        return region.contains(x, y, z)
    }
}