package me.elsiff.morefish.listener

import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
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
    private val fishTypes: FishTypeTable,
    private val catchEffects: CatchEffectCollection,
    private val converter: FishItemStackConverter
) : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state == PlayerFishEvent.State.CAUGHT_FISH && event.caught is Item) {
            val fish = fishTypes.pickRandomType().generateFish()
            catchEffects.playAll(event.player, fish)
            (event.caught as Item).itemStack = converter.createItemStack(fish, event.player, LocalDateTime.now())
        }
    }
}