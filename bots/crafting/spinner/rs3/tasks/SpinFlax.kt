package com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.active
import com.qverkk.kotlin.api.methods.util.refreshing
import com.runemate.game.api.hybrid.input.Keyboard
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.queries.GameObjectQueryBuilder
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask
import java.awt.event.KeyEvent

class SpinFlax(val builder: GameObjectQueryBuilder) : LeafTask() {

    private val component by refreshing { Interfaces.newQuery().containers(1371).results().first() }

    override fun execute() {
        val me = Players.getLocal() ?: return
        val spinner = getWheel() ?: return
        if (component != null) {
            if (Keyboard.typeKey(KeyEvent.VK_SPACE)) {
                Execution.delayWhile({ Inventory.contains("Flax") }, { me.active() }, 3000, 4000)
            }
        } else if (spinner.interactAndTurn("Spin")) {
            Execution.delayWhile({ Interfaces.newQuery().containers(1371).results().isEmpty() }, { me.active() }, 2000, 3000)
        }
    }

    private fun getWheel() = builder.results().first()
}