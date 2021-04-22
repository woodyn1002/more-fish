package me.elsiff.morefish.update

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

/**
 * Created by elsiff on 2019-01-03.
 */
class UpdateChecker(
    projectId: Int,
    private val currentVersion: String
) {
    private val checkUrl = URL("https://api.spigotmc.org/legacy/update.php?resource=$projectId")
    lateinit var newVersion: String

    fun check() {
        val connection = checkUrl.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use {
            newVersion = it.readLine()
        }
    }

    fun hasNewVersion(): Boolean {
        return newVersion != currentVersion
    }
}
