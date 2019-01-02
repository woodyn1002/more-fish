package me.elsiff.morefish.resource.template

import me.elsiff.morefish.resource.configuration.getTextListTemplate
import me.elsiff.morefish.resource.configuration.getTextTemplate
import org.bukkit.configuration.Configuration
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

/**
 * Created by elsiff on 2019-01-02.
 */
class TemplateBundle(
        lang: Configuration
) {
    val catchFish = lang.getTextTemplate("catch-fish")
    val get1st = lang.getTextTemplate("get-1st")
    val noFishingAllowed = lang.getTextTemplate("no-fishing-allowed")
    val contestStart = lang.getTextTemplate("contest-start")
    val contestStartTimer = lang.getTextTemplate("contest-start-timer")
    val contestStop = lang.getTextTemplate("contest-stop")
    val timerBossBar = lang.getTextTemplate("timer-boss-bar")
    val timeFormatMinutes = lang.getString("time-format-minutes")
    val timeFormatSeconds = lang.getString("time-format-seconds")
    val topList = lang.getTextTemplate("top-list")
    val topNoRecord = lang.getTextTemplate("top-no-record")
    val topMine = lang.getTextTemplate("top-mine")
    val topMineNoRecord = lang.getTextTemplate("top-mine-no-record")
    val reward = lang.getTextTemplate("reward")
    val rewardCashPrize = lang.getTextTemplate("reward-cash-prize")
    val noPermission = lang.getTextTemplate("no-permission")
    val alreadyOngoing = lang.getTextTemplate("already-ongoing")
    val alreadyStopped = lang.getTextTemplate("already-stopped")
    val notNumber = lang.getTextTemplate("not-number")
    val notPositive = lang.getTextTemplate("not-positive")
    val notOngoing = lang.getTextTemplate("not-ongoing")
    val clearRecords = lang.getTextTemplate("clear-records")
    val reloadConfig = lang.getTextTemplate("reload-config")
    val failedToReload = lang.getTextTemplate("failed-to-reload")
    val inGameCommand = lang.getTextTemplate("in-game-command")
    val shopDisabled = lang.getTextTemplate("shop-disabled")
    val playerNotFound = lang.getTextTemplate("player-not-found")
    val forcedPlayerToShop = lang.getTextTemplate("forced-player-to-shop")
    val invalidCommand = lang.getTextTemplate("invalid-command")
    val rewardsGuiTitle = lang.getTextTemplate("rewards-gui-title")
    val rewardsGuideIconName = lang.getTextTemplate("rewards-guide-icon-name")
    val rewardsGuideIconLore = lang.getTextListTemplate("rewards-guide-icon-lore")
    val rewardsSignIconName = lang.getTextTemplate("rewards-sign-icon-name")
    val rewardsSignIconLore = lang.getTextListTemplate("rewards-sign-icon-lore")
    val rewardsConsolation = lang.getTextTemplate("rewards-consolation")
    val rewardsEmeraldIconName = lang.getTextTemplate("rewards-emerald-icon-name")
    val rewardsEmeraldIconLore = lang.getTextListTemplate("rewards-emerald-icon-lore")
    val enterCashPrize = lang.getTextListTemplate("enter-cash-prize")
    val enteredCancel = lang.getTextTemplate("entered-cancel")
    val enteredNotNumber = lang.getTextTemplate("entered-not-number")
    val enteredNotPositive = lang.getTextTemplate("entered-not-positive")
    val enteredSuccessfully = lang.getTextTemplate("entered-successfully")
    val savedChanges = lang.getTextTemplate("saved-changes")
    val shopGuiTitle = lang.getTextTemplate("shop-gui-title")
    val shopEmeraldIconName = lang.getTextTemplate("shop-emerald-icon-name")
    val shopSold = lang.getTextTemplate("shop-sold")
    val shopNoFish = lang.getTextTemplate("shop-no-fish")
    val createdSignShop = lang.getTextTemplate("created-sign-shop")
    val oldFile = lang.getTextTemplate("old-file")
    val newVersion = lang.getTextListTemplate("new-version")

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