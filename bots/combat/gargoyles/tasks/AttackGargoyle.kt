package com.qverkk.kotlin.bots.combat.gargoyles.tasks

import com.qverkk.kotlin.api.methods.entities.*
import com.qverkk.kotlin.api.methods.local.active
import com.qverkk.kotlin.api.methods.local.localPlayer
import com.qverkk.kotlin.bots.combat.gargoyles.Gargoyles
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.entities.Player
import com.runemate.game.api.hybrid.queries.NpcQueryBuilder
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask
import java.util.*

object AttackGargoyle : LeafTask() {

    override fun execute() {
        val player = localPlayer() ?: return
        val query = npcQuery().within(Gargoyles.fightingArea).names("Gargoyle", "*Gargoyle").actions("Attack") ?: return
        val currentTarget = npcAttackingMe(query, player) ?: npcWithoutTarget(query) ?: return
        if (currentTarget.interactAndTurn("Attack")) {
            Execution.delayUntil({ Objects.equals(player.target, currentTarget) }, { player.active() }, 1200, 1800)
        }
    }

    private fun npcAttackingMe(query: NpcQueryBuilder, player: Player): Npc? = queryOf(query).attacking(player).findNearest()

    private fun npcWithoutTarget(query: NpcQueryBuilder): Npc? = queryOf(query).filter { it.target == null && it.animationId == -1 }.findNearest()
}