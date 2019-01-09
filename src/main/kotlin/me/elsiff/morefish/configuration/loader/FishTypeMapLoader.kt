package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.configuration.translated
import me.elsiff.morefish.fishing.FishRarity
import me.elsiff.morefish.fishing.FishType
import me.elsiff.morefish.fishing.condition.Condition

/**
 * Created by elsiff on 2019-01-09.
 */
class FishTypeMapLoader(
    private val fishRaritySetLoader: FishRaritySetLoader,
    private val customItemStackLoader: CustomItemStackLoader
) : CustomLoader<Map<FishRarity, Set<FishType>>> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Map<FishRarity, Set<FishType>> {
        section[path].let { root ->
            val rarities = fishRaritySetLoader.loadFrom(root, "rarity-list")
            return root["fish-list"].children.map { groupByRarity ->
                val rarity = findRarity(rarities, groupByRarity.name)
                val fishTypes = groupByRarity.children.map {
                    FishType(
                        name = it.name,
                        displayName = it.string("display-name").translated(),
                        rarity = rarity,
                        lengthMin = it.double("length-min"),
                        lengthMax = it.double("length-max"),
                        icon = customItemStackLoader.loadFrom(it, "icon"),
                        feature = FishType.Feature(
                            skipItemFormat = it.boolean("skip-item-format", false),
                            commands = it.strings("commands", emptyList()).map(String::translated),
                            conditions = it.strings("conditions", emptyList()).map(Condition.Companion::valueOf)
                        )
                    )
                }.toSet()
                Pair(rarity, fishTypes)
            }.toMap()
        }
    }

    private fun findRarity(rarities: Set<FishRarity>, name: String): FishRarity {
        return rarities.find { it.name == name } ?: throw IllegalStateException("Rarity '$name' doesn't exist")
    }
}