package com.qverkk.kotlin.bots.aio.smither.settings

import com.runemate.game.api.hybrid.Environment.getForumName
import com.runemate.game.api.hybrid.RuneScape
import com.runemate.game.api.hybrid.player_sense.PlayerSense
import com.runemate.game.api.hybrid.region.Players
import kotlin.random.Random


class CustomPlayerSense {

    enum class Key(val playerSenseKey: String) {
        ACTION_BAR_SPAM("qve_smither_abs"),
        BANKER_PREFERENCE("qve_smither_bp"),
        DOUBLE_CLICK("qve_smither_dc"),
        VIEW_PORT_WALKING("qve_smither_vpw");
    }

    companion object {
        var playerSenseInitialized = false

        fun initialize() {
            val seed: Int
            if (RuneScape.isLoggedIn()) {
                seed = (sumBytes(getForumName()) or sumBytes(Players.getLocal()!!.name)) * sumBytes(Players.getLocal()!!.name)
                val random = Random(seed)
                PlayerSense.put(Key.ACTION_BAR_SPAM.playerSenseKey, random.nextInt(100))
                PlayerSense.put(Key.BANKER_PREFERENCE.playerSenseKey, random.nextInt(100))
                PlayerSense.put(Key.DOUBLE_CLICK.playerSenseKey, random.nextInt(35) + 10)
                PlayerSense.put(Key.VIEW_PORT_WALKING.playerSenseKey, random.nextInt(15))

                playerSenseInitialized = true
            } else {
                seed = sumBytes(getForumName())
                val random = Random(seed)
                PlayerSense.put(Key.ACTION_BAR_SPAM.playerSenseKey, random.nextInt(100))
                PlayerSense.put(Key.BANKER_PREFERENCE.playerSenseKey, random.nextInt(100))
                PlayerSense.put(Key.DOUBLE_CLICK.playerSenseKey, random.nextInt(35) + 10)
                PlayerSense.put(Key.VIEW_PORT_WALKING.playerSenseKey, random.nextInt(15))
            }
        }

        private fun sumBytes(string: String?): Int {
            if (string == null) return 0

            var value = 0
            for (i in 0 until string.length) {
                value += string[i].toInt() * i
            }
            return value
        }
    }
}