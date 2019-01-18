package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.announcement.PlayerAnnouncement
import me.elsiff.morefish.configuration.ConfigurationValueAccessor

/**
 * Created by elsiff on 2019-01-15.
 */
class PlayerAnnouncementLoader : CustomLoader<PlayerAnnouncement> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): PlayerAnnouncement {
        val configuredValue = section.double(path)
        return when (configuredValue.toInt()) {
            -2 -> PlayerAnnouncement.ofEmpty()
            -1 -> PlayerAnnouncement.ofServerBroadcast()
            0 -> PlayerAnnouncement.ofBaseOnly()
            else -> PlayerAnnouncement.ofRanged(configuredValue)
        }
    }
}