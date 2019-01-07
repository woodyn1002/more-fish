package me.elsiff.morefish.util

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * Created by elsiff on 2019-01-06.
 */
class OneTickScheduler(
        private val plugin: Plugin
) {
    private val runnableMap = mutableMapOf<Any, MutableSet<OneTickRunnable>>()

    fun scheduleLater(client: Any, action: () -> Unit) {
        val runnable = OneTickRunnable(client, action)
        runnable.runTaskLater(plugin, 1)

        if (!runnableMap.containsKey(runnable)) {
            runnableMap[client] = mutableSetOf(runnable)
        } else {
            runnableMap[client]!!.add(runnable)
        }
    }

    fun cancelAllOf(client: Any) {
        runnableMap[client]?.filter { !it.isCancelled }?.forEach { it.cancel() }
        runnableMap.remove(client)
    }

    private inner class OneTickRunnable(
            private val client: Any,
            private val action: () -> Unit
    ) : BukkitRunnable() {
        override fun run() {
            action()
            runnableMap[client]?.remove(this)
        }
    }
}