package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.configuration.translated
import me.elsiff.morefish.fishing.FishRarity
import me.elsiff.morefish.fishing.catchhandler.CatchCommandExecutor
import me.elsiff.morefish.fishing.catchhandler.CatchHandler

/**
 * Created by elsiff on 2019-01-09.
 */
class FishRaritySetLoader(
    private val chatColorLoader: ChatColorLoader,
    private val playerAnnouncementLoader: PlayerAnnouncementLoader
) : CustomLoader<Set<FishRarity>> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Set<FishRarity> {
        return section[path].children.map {
            val catchHandlers = mutableListOf<CatchHandler>()

            if (it.contains("commands")) {
                val handler = CatchCommandExecutor(it.strings("commands").translated())
                catchHandlers.add(handler)
            }
            FishRarity(
                name = it.name,
                displayName = it.string("display-name").translated(),
                default = it.boolean("default", false),
                probability = it.double("chance", 0.0) / 100.0,
                color = chatColorLoader.loadFrom(it, "color"),
                catchHandlers = catchHandlers,
                catchAnnouncement = playerAnnouncementLoader.loadIfExists(it, "catch-announce")
                    ?: Config.defaultCatchAnnouncement,
                hasNotFishItemFormat = it.boolean("skip-item-format", false),
                noDisplay = it.boolean("no-display", false),
                hasCatchFirework = it.boolean("firework", false),
                additionalPrice = it.double("additional-price", 0.0)
            )
        }.toSet()
    }
}