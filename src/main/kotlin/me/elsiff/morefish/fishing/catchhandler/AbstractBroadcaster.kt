package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.announcement.PlayerAnnouncement
import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishType
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
abstract class AbstractBroadcaster : CatchHandler {
    abstract val catchMessageFormat: TextFormat

    abstract fun meetBroadcastCondition(catcher: Player, fish: Fish): Boolean

    abstract fun announcement(fish: Fish): PlayerAnnouncement

    override fun handle(catcher: Player, fish: Fish) {
        if (meetBroadcastCondition(catcher, fish)) {
            val receivers = fish.type.catchAnnouncement.receiversOf(catcher).toMutableList()

            if (Config.standard.boolean("messages.only-announce-fishing-rod")) {
                receivers.removeIf { it.inventory.itemInMainHand.type != Material.FISHING_ROD }
            }

            val msg = catchMessageFormat.replace(
                "%player%" to catcher.name,
                "%length%" to fish.length,
                "%rarity%" to fish.type.rarity.displayName.toUpperCase(),
                "%rarity_color%" to fish.type.rarity.color,
                "%fish%" to fish.type.displayName,
                "%fish_with_rarity%" to fishNameWithRarity(fish.type)
            ).output(catcher)
            for (receiver in receivers) {
                receiver.sendMessage(msg)
            }
        }
    }

    private fun fishNameWithRarity(fishType: FishType): String {
        return (if (fishType.noDisplay) "" else fishType.rarity.displayName.toUpperCase() + " ") +
            fishType.displayName
    }
}
