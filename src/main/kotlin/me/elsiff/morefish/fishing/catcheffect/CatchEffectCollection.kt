package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class CatchEffectCollection(
    private val competition: FishingCompetition
) {
    private val _effects: MutableList<CatchHandler> = mutableListOf()
    val effects: List<CatchHandler>
        get() = _effects

    init {
        clear()
        addEffect(BroadcastEffect())
        addEffect(CompetitionEffect(competition))
    }

    fun addEffect(effect: CatchHandler) {
        _effects.add(effect)
    }

    fun removeEffect(effect: CatchHandler) {
        check(effect in _effects) { "Catch Effect doesn't exist" }

        _effects.remove(effect)
    }

    fun clear() {
        _effects.clear()
    }

    fun playAll(catcher: Player, fish: Fish) {
        for (effect in _effects) {
            effect.play(catcher, fish)
        }
    }
}