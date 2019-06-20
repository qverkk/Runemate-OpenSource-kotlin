package com.qverkk.kotlin.bots.combat.gargoyles.tasks

import com.qverkk.kotlin.bots.combat.gargoyles.Gargoyles
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.queries.SpriteItemQueryBuilder
import com.runemate.game.api.rs3.local.hud.Powers
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

class AlchItems(private val itemBuilder: SpriteItemQueryBuilder) : LeafTask() {

    //lateinit var items: SpriteItem

    companion object {
        private val bot = Environment.getBot() as Gargoyles
    }

    override fun execute() {
        val items = getItem() ?: return
        if (!items.isValid) return
        val itemName = items.definition?.name ?: return
        if (Powers.Magic.HIGH_LEVEL_ALCHEMY.isSelected || Gargoyles.ability.activate("High Level Alchemy")) {
            if (items.interact("Cast", "High Level Alchemy -> $itemName")) {
                Execution.delayUntil({ items.isValid.not() }, 1200, 1800)
            }
        }
    }

    private fun getItem() = itemBuilder.results().first()
}