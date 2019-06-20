package com.qverkk.kotlin.bots.crafting.slayerrings.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.localPlayer
import com.qverkk.kotlin.api.methods.util.refreshing
import com.qverkk.kotlin.bots.crafting.slayerrings.SlayerRings
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

object GetMaterials : LeafTask() {

    val bot = Environment.getBot() as SlayerRings

    private val bankBooth by refreshing { GameObjects.newQuery().names("Bank chest").actions("Use").results().nearest() }

    override fun execute() {
        val bankOpen = Bank.isOpen()
        val player = localPlayer() ?: return
        when {
            MakeXInterface.isOpen() -> MakeXInterface.close(true)
            bankOpen -> {
                if (Bank.containsAllOf("Gold bar", "Enchanted gem").not()) {
                    bot.platform.invokeLater({ bot.stop("No gold bars or gems") })
                } else if (Bank.loadPreset(1, true)) {
                    Execution.delayUntil({ Inventory.contains("Gold bar") }, 2000, 3000)
                }
            }
            !bankOpen -> {
                val bankBooth = bankBooth ?: return
                if (bankBooth.interactAndTurn("Use")) {
                    Execution.delayUntil({ Bank.isOpen() }, { player.isMoving }, 2000, 3000)
                }
            }
        }
    }
}