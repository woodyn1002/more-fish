package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.math.min

/**
 * Created by elsiff on 2018-12-25.
 */
class FishingCompetition(
    private val plugin: Plugin
) {
    private val records: TreeSet<Record> = sortedSetOf(Comparator.reverseOrder())
    val ranking: List<Record>
        get() = records.toList()
    var state: State = State.DISABLED
    private var timerTask: BukkitTask? = null

    fun enable() {
        checkStateDisabled()

        records.clear()
        state = State.ENABLED
    }

    fun enableWithTimer(tick: Long) {
        timerTask = plugin.server.scheduler.runTaskLater(plugin, this::disable, tick)
        enable()
    }

    fun disable() {
        checkStateEnabled()

        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null
        }
        state = State.DISABLED
    }

    fun willBeNewFirst(catcher: Player, fish: Fish): Boolean {
        return records.isEmpty() || records.first().let { fish.length > it.fish.length && it.fisher != catcher }
    }

    fun putRecord(record: Record) {
        checkStateEnabled()

        if (containsRecord(record.fisher)) {
            val oldRecord = recordOf(record.fisher)
            if (record.fish.length > oldRecord.fish.length) {
                records.remove(oldRecord)
                records.add(record)
            }
        } else {
            records.add(record)
        }
    }

    fun containsRecord(fisher: Player): Boolean {
        return records.any { it.fisher == fisher }
    }

    fun recordOf(fisher: Player): Record {
        for (record in records) {
            if (record.fisher == fisher) {
                return record
            }
        }
        throw IllegalStateException("Record not found")
    }

    fun rankedRecordOf(fisher: Player): Pair<Int, Record> {
        for ((index, record) in records.withIndex()) {
            if (record.fisher == fisher) {
                return Pair(index + 1, record)
            }
        }
        throw IllegalStateException("Record not found")
    }

    fun recordOf(rankNumber: Int): Record {
        require(rankNumber >= 1 && rankNumber <= records.size) { "Rank number is out of records size" }
        return records.elementAt(rankNumber - 1)
    }

    fun top(size: Int): List<Record> {
        return records.toList().subList(0, min(size, records.size))
    }

    fun clear() {
        records.clear()
    }

    private fun checkStateEnabled() {
        check(state == State.ENABLED) { "Fishing competition hasn't enabled" }
    }

    private fun checkStateDisabled() {
        check(state == State.DISABLED) { "Fishing competition hasn't disabled" }
    }

    enum class State { ENABLED, DISABLED }
}