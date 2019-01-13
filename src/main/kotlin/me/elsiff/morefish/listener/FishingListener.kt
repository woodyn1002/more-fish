package me.elsiff.morefish.listener

import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.item.FishItemStackConverter
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import java.time.LocalDateTime

/**
 * Created by elsiff on 2018-12-24.
 */
class FishingListener(
    private val fishTypeTable: FishTypeTable,
    private val catchEffects: CatchEffectCollection,
    private val converter: FishItemStackConverter,
    private val competition: FishingCompetition
) : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state == PlayerFishEvent.State.CAUGHT_FISH && event.caught is Item) {
            val caught = event.caught as Item
            val fish = fishTypeTable.pickRandomType(caught, event.player, competition).generateFish()
            catchEffects.playAll(event.player, fish)
            caught.itemStack = converter.createItemStack(fish, event.player, LocalDateTime.now())
        }
    }
}