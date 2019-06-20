package com.qverkk.kotlin.bots.crafting.slayerrings.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.active
import com.qverkk.kotlin.api.methods.local.localPlayer
import com.qverkk.kotlin.api.methods.util.refreshing
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

object Smelt : LeafTask() {

    private val furnace by refreshing { GameObjects.newQuery().names("Furnace").actions("Smelt").results().nearest() }

    override fun execute() {
        val player = localPlayer() ?: return
        val interfaceOpen = MakeXInterface.isOpen()
        when {
            Bank.isOpen() -> Bank.close()
            interfaceOpen && MakeXInterface.confirm(true) ->
                Execution.delayUntil({ Inventory.containsAllOf("Gold bar", "Enchanted gem").not() }, { player.active() }, 2000, 3000)
            !interfaceOpen -> {
                val furnace = furnace ?: return
                if (furnace.interactAndTurn("Smelt")) {
                    Execution.delayUntil({ MakeXInterface.isOpen() }, { player.isMoving }, 2000, 3000)
                }
            }
        }
    }
}