package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishType
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
abstract class AbstractBroadcaster(
    private val pathOfLangFormat: String,
    private val pathOfAnnounceRange: String
) : CatchHandler {
    abstract fun hasBroadcastCondition(catcher: Player, fish: Fish): Boolean

    override fun handle(catcher: Player, fish: Fish) {
        if (hasBroadcastCondition(catcher, fish)) {
            for (receiver in receiversOf(catcher)) {
                val msg = Lang.format(pathOfLangFormat).replace(
                    "%player%" to catcher.name,
                    "%length%" to fish.length,
                    "%rarity%" to fish.type.rarity.displayName.toUpperCase(),
                    "%rarity_color%" to fish.type.rarity.color,
                    "%fish%" to fish.type.name,
                    "%fish_with_rarity%" to fishNameWithRarity(fish.type)
                ).output
                receiver.sendMessage(msg)
            }
        }
    }

    private fun receiversOf(catcher: Player): List<Player> {
        val receivers = mutableListOf<Player>()

        val radius = Config.standard.int(pathOfAnnounceRange)
        val isServerBroadcast = (radius == -1)

        if (isServerBroadcast) {
            receivers.addAll(catcher.server.onlinePlayers)
        } else {
            receivers.addAll(catcher.world.players.filter { it.location.distance(catcher.location) <= radius })
        }
        if (Config.standard.boolean("messages.only-announce-fishing-rod")) {
            receivers.removeIf { it.inventory.itemInMainHand?.type != Material.FISHING_ROD }
        }
        return receivers
    }

    private fun fishNameWithRarity(fishType: FishType): String {
        return (if (fishType.noDisplay) "" else fishType.rarity.displayName.toUpperCase() + " ") +
                fishType.displayName
    }
}