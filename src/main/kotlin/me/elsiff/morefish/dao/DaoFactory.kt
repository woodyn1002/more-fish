package me.elsiff.morefish.dao

import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.dao.yaml.YamlRecordDao

/**
 * Created by elsiff on 2019-01-18.
 */
object DaoFactory {
    private lateinit var moreFish: MoreFish

    val records: RecordDao
        get() = YamlRecordDao(moreFish, moreFish.fishTypeTable)

    fun init(moreFish: MoreFish) {
        this.moreFish = moreFish
    }
}