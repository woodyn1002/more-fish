package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class CatchEffectCollection(
        private val competition: FishingCompetition
) : ResourceReceiver {
    val effects: List<CatchEffect> = mutableListOf()

    override fun receiveResource(resources: ResourceBundle) {
        clear()
        addEffect(BroadcastEffect())
        addEffect(CompetitionEffect(competition))
    }

    fun addEffect(effect: CatchEffect) {
        effects as MutableList
        effects.add(effect)
    }

    fun removeEffect(effect: CatchEffect) {
        check(effect in effects) { "Catch Effect doesn't exist" }

        effects as MutableList
        effects.remove(effect)
    }

    fun clear() {
        effects as MutableList
        effects.clear()
    }

    fun playAll(catcher: Player, fish: Fish) {
        effects.forEach { it.play(catcher, fish) }
    }
}