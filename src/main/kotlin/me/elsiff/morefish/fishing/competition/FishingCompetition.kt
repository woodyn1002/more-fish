package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/**
 * Created by elsiff on 2018-12-25.
 */
class FishingCompetition(
        private val plugin: Plugin
) : ResourceReceiver {
    enum class State { ENABLED, DISABLED }

    private val records = sortedSetOf<Record>(Comparator.reverseOrder())
    var state = State.DISABLED
    var timerTask: BukkitTask? = null

    override fun receiveResource(resources: ResourceBundle) {

    }

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

    fun putRecord(record: Record) {
        checkStateEnabled()

        records.removeIf { it.fisher == record.fisher && it.fish.length > record.fish.length }
        records.add(record)
    }

    fun getRecord(fisher: Player): Record {
        for (record in records) {
            if (record.fisher == fisher) {
                return record
            }
        }
        throw IllegalStateException("Record not found")
    }

    fun getRecordRanked(fisher: Player): Pair<Int, Record> {
        for ((index, record) in records.withIndex()) {
            if (record.fisher == fisher) {
                return Pair(index + 1, record)
            }
        }
        throw IllegalStateException("Record not found")
    }

    fun getRecord(rankNumber: Int): Record {
        require(rankNumber >= 1 && rankNumber <= records.size) { "Rank number is out of records size" }
        return records.elementAt(rankNumber - 1)
    }

    fun ranking(): List<Record> {
        return records.toList()
    }

    fun top(size: Int): List<Record> {
        return records.toList().subList(0, size)
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
}