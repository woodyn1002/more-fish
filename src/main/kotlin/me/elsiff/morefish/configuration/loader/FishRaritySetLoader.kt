package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.configuration.translated
import me.elsiff.morefish.fishing.FishRarity

/**
 * Created by elsiff on 2019-01-09.
 */
class FishRaritySetLoader(
    private val chatColorLoader: ChatColorLoader
) : CustomLoader<Set<FishRarity>> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Set<FishRarity> {
        return section[path].children.map {
            FishRarity(
                name = it.name,
                displayName = it.string("display-name").translated(),
                default = it.boolean("default", false),
                probability = it.double("chance", 0.0) / 100.0,
                color = chatColorLoader.loadFrom(it, "color"),
                feature = FishRarity.Feature(
                    additionalPrice = it.double("additional-price", 0.0),
                    noBroadcast = it.boolean("no-broadcast", false),
                    noDisplay = it.boolean("no-display", false),
                    firework = it.boolean("firework", false)
                )
            )
        }.toSet()
    }
}