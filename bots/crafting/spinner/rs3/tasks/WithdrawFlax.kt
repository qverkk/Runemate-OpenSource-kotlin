package com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.util.refreshing
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.region.Banks
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

class WithdrawFlax(private val locatable: Locatable) : LeafTask() {

    val bank by refreshing { Banks.getLoadedBankBooths().nearest() }

    override fun execute() {
        val bank = bank ?: return
        val me = Players.getLocal() ?: return
        if (Bank.isOpen()) {
            if (Bank.loadPreset(1, true)) {
                Execution.delayUntil({ Inventory.contains("Flax") }, 1200, 1800)
            }
        } else if (bank.interactAndTurn("Bank", locatable)) {
            Execution.delayWhile({ Bank.isOpen().not() }, { me.isMoving }, 2000, 3000)
        }
    }
}