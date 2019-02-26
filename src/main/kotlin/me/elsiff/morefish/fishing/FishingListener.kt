package me.elsiff.morefish.fishing

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.fishing.catchhandler.CatchHandler
import me.elsiff.morefish.fishing.catchhandler.CompetitionRecordAdder
import me.elsiff.morefish.fishing.catchhandler.NewFirstBroadcaster
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.item.FishItemStackConverter
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

/**
 * Created by elsiff on 2018-12-24.
 */
class FishingListener(
    private val fishTypeTable: FishTypeTable,
    private val converter: FishItemStackConverter,
    private val competition: FishingCompetition,
    private val globalCatchHandlers: List<CatchHandler>
) : Listener {
    private val fishMaterials: List<Material> = listOf(
        Material.COD, Material.SALMON, Material.PUFFERFISH, Material.TROPICAL_FISH
    )
    private val replacingVanillaConditions: List<(PlayerFishEvent) -> Boolean> = listOf<(PlayerFishEvent) -> Boolean>(
        {
            if (Config.standard.boolean("general.only-for-contest"))
                competition.isEnabled()
            else
                true
        },
        {
            if (Config.standard.boolean("general.replace-only-fish"))
                (it.caught as Item).itemStack.type in fishMaterials
            else
                true
        },
        {
            val event = it
            val disabledWorlds = Config.standard.strings("general.disabled-worlds", listOf()).map{ event.player.server.getWorld(it) }
            disabledWorlds.isEmpty() || event.player.world !in disabledWorlds
        },
        {
            Config.redProtectApi == null
                    || Config.standard.strings("general.restrict-to-regions").isEmpty()
                    || (Config.redProtectApi?.getRegion(it.player.location) != null && Config.redProtectApi?.getRegion(it.player.location)?.name in Config.standard.strings("general.restrict-to-regions"))
        }
    )

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state == PlayerFishEvent.State.CAUGHT_FISH && event.caught is Item) {
            if (Config.standard.boolean("general.no-fishing-unless-contest") && !competition.isEnabled()) {
                event.isCancelled = true
                event.player.sendMessage(Lang.text("no-fishing-allowed"))
            } else if (canReplaceVanillaFishing(event)) {
                val caught = event.caught as Item
                val fish = fishTypeTable.pickRandomType(caught, event.player, competition).generateFish()

                for (handler in catchHandlersOf(event, fish)) {
                    handler.handle(event.player, fish)
                }
                caught.itemStack = converter.createItemStack(fish, event.player)
            }
        }
    }

    private fun canReplaceVanillaFishing(event: PlayerFishEvent): Boolean {
        return replacingVanillaConditions.all { it(event) }
    }

    private fun catchHandlersOf(event: PlayerFishEvent, fish: Fish): Collection<CatchHandler> {
        val catchHandlers = globalCatchHandlers + fish.type.catchHandlers

        val contestDisabledWorlds = Config.standard.strings("general.contest-disabled-worlds")
            .map { event.player.server.getWorld(it) }
        return if (event.player.world in contestDisabledWorlds) {
            catchHandlers.filter {
                it !is CompetitionRecordAdder && it !is NewFirstBroadcaster
            }
        } else {
            catchHandlers
        }
    }
}