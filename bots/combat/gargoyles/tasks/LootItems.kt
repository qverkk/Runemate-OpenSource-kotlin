package com.qverkk.kotlin.bots.combat.gargoyles.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.active
import com.runemate.game.api.hybrid.queries.GroundItemQueryBuilder
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

class LootItems(private val builder: GroundItemQueryBuilder) : LeafTask() {

    override fun execute() {
        val item = getItem() ?: return
        val player = Players.getLocal() ?: return
        if (item.interactAndTurn("Take")) {
            Execution.delayWhile({ item.isValid }, { player.active() }, 1200, 1800)
        }
    }

    private fun getItem() = builder.results().nearest()
}