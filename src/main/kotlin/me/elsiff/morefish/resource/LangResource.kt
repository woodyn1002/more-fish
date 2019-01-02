package me.elsiff.morefish.resource

import me.elsiff.morefish.resource.configuration.FileConfigurationHandler
import me.elsiff.morefish.resource.template.TextListTemplate
import me.elsiff.morefish.resource.template.TextTemplate
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

/**
 * Created by elsiff on 2018-12-28.
 */

class LangResource : FileConfigurationHandler() {
    inner class TextTemplateValue(id: String) : Value<TextTemplate>(id,
            { cfg, path -> TextTemplate(cfg.getString(path)) })

    inner class TextListTemplateValue(id: String) : Value<TextListTemplate>(id,
            { cfg, path -> TextListTemplate(cfg.getStringList(path)) })

    val version by IntValue("version")
    val catchFish by TextTemplateValue("catch-fish")
    val get1st by TextTemplateValue("get-1st")
    val noFishingAllowed by TextTemplateValue("no-fishing-allowed")
    val contestStart by TextTemplateValue("contest-start")
    val contestStartTimer by TextTemplateValue("contest-start-timer")
    val contestStop by TextTemplateValue("contest-stop")
    val timerBossBar by TextTemplateValue("timer-boss-bar")
    val timeFormatMinutes by StringValue("time-format-minutes")
    val timeFormatSeconds by StringValue("time-format-seconds")
    val topList by TextTemplateValue("top-list")
    val topNoRecord by TextTemplateValue("top-no-record")
    val topMine by TextTemplateValue("top-mine")
    val topMineNoRecord by TextTemplateValue("top-mine-no-record")
    val reward by TextTemplateValue("reward")
    val rewardCashPrize by TextTemplateValue("reward-cash-prize")
    val noPermission by TextTemplateValue("no-permission")
    val alreadyOngoing by TextTemplateValue("already-ongoing")
    val alreadyStopped by TextTemplateValue("already-stopped")
    val notNumber by TextTemplateValue("not-number")
    val notPositive by TextTemplateValue("not-positive")
    val notOngoing by TextTemplateValue("not-ongoing")
    val clearRecords by TextTemplateValue("clear-records")
    val reloadConfig by TextTemplateValue("reload-config")
    val failedToReload by TextTemplateValue("failed-to-reload")
    val inGameCommand by TextTemplateValue("in-game-command")
    val shopDisabled by TextTemplateValue("shop-disabled")
    val playerNotFound by TextTemplateValue("player-not-found")
    val forcedPlayerToShop by TextTemplateValue("forced-player-to-shop")
    val invalidCommand by TextTemplateValue("invalid-command")
    val rewardsGuiTitle by TextTemplateValue("rewards-gui-title")
    val rewardsGuideIconName by TextTemplateValue("rewards-guide-icon-name")
    val rewardsGuideIconLore by TextListTemplateValue("rewards-guide-icon-lore")
    val rewardsSignIconName by TextTemplateValue("rewards-sign-icon-name")
    val rewardsSignIconLore by TextListTemplateValue("rewards-sign-icon-lore")
    val rewardsConsolation by TextTemplateValue("rewards-consolation")
    val rewardsEmeraldIconName by TextTemplateValue("rewards-emerald-icon-name")
    val rewardsEmeraldIconLore by TextListTemplateValue("rewards-emerald-icon-lore")
    val enterCashPrize by TextListTemplateValue("enter-cash-prize")
    val enteredCancel by TextTemplateValue("entered-cancel")
    val enteredNotNumber by TextTemplateValue("entered-not-number")
    val enteredNotPositive by TextTemplateValue("entered-not-positive")
    val enteredSuccessfully by TextTemplateValue("entered-successfully")
    val savedChanges by TextTemplateValue("saved-changes")
    val shopGuiTitle by TextTemplateValue("shop-gui-title")
    val shopEmeraldIconName by TextTemplateValue("shop-emerald-icon-name")
    val shopSold by TextTemplateValue("shop-sold")
    val shopNoFish by TextTemplateValue("shop-no-fish")
    val createdSignShop by TextTemplateValue("created-sign-shop")
    val oldFile by TextTemplateValue("old-file")
    val newVersion by TextListTemplateValue("new-version")

    fun formatTime(second: Long): String {
        val builder = DateTimeFormatterBuilder()
        if (second / 60 > 0) {
            builder.appendValue(ChronoField.MINUTE_OF_HOUR)
                    .appendLiteral(timeFormatMinutes)
                    .appendLiteral(" ")
        }
        builder.appendValue(ChronoField.SECOND_OF_MINUTE)
                .appendLiteral(timeFormatSeconds)
        return LocalTime.ofSecondOfDay(second).format(builder.toFormatter())
    }
}