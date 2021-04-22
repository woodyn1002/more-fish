package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class TimeCondition(
    private val state: TimeState
) : FishCondition {
    override fun check(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition
    ): Boolean {
        return TimeState.fromTime(caught.world.time) == state
    }

    enum class TimeState(
        vararg val range: IntRange
    ) {
        DAY(1000 until 13000),
        NIGHT(0 until 1000, 13000 until 24000);

        companion object {
            fun fromTime(worldTime: Long): TimeState? {
                return values().find { state ->
                    state.range.any { it.contains(worldTime) }
                }
            }
        }
    }
}
