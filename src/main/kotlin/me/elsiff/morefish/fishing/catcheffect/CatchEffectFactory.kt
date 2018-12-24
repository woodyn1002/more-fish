package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class CatchEffectFactory {
    val effects = mutableListOf<CatchEffect>()

    fun addEffect(effect: CatchEffect) = effects.add(effect)

    fun removeEffect(effect: CatchEffect) {
        if (effect !in effects)
            throw IllegalStateException("Catch Effect doesn't exist")
        else
            effects.remove(effect)
    }

    fun playAll(catcher: Player, fish: Fish) {
        effects.forEach { it.play(catcher, fish) }
    }
}