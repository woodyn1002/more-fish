package me.elsiff.morefish.fishing.competition

/**
 * Created by elsiff on 2018-12-25.
 */
class FishingCompetition {
    enum class State { ENABLED, DISABLED }

    private val records = sortedSetOf<Record>(Comparator.reverseOrder())
    var state = State.DISABLED

    fun enable() {
        records.clear()
        state = State.ENABLED
    }

    fun disable() {
        state = State.DISABLED
    }

    fun putRecord(record: Record) {
        if (state != State.ENABLED) {
            throw IllegalStateException("Fishing competition hasn't enabled")
        }
        records.removeIf { it.fisher == record.fisher && it.fish.length > record.fish.length }
        records.add(record)
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
}