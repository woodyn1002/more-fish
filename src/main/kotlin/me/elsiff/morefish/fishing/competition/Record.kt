package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.fishing.Fish
import org.bukkit.OfflinePlayer

/**
 * Created by elsiff on 2018-12-25.
 */
class Record(val fisher: OfflinePlayer, val fish: Fish) : Comparable<Record> {
    override fun compareTo(other: Record): Int {
        return fish.length.compareTo(other.fish.length)
    }
}