package me.elsiff.morefish.listener

import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import me.elsiff.morefish.resource.template.TemplateBundle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created by elsiff on 2019-01-03.
 */
class UpdateNotifierListener(
        val newVersion: String
) : Listener, ResourceReceiver {
    private lateinit var templates: TemplateBundle

    override fun receiveResource(resources: ResourceBundle) {
        templates = resources.templates
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp) {
            templates.newVersion.formatted(mapOf("%s" to newVersion)).forEach { msg ->
                event.player.sendMessage(msg)
            }
        }
    }
}