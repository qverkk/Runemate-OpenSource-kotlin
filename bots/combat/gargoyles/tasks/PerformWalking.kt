package com.qverkk.kotlin.bots.combat.gargoyles.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.active
import com.qverkk.kotlin.api.methods.traversal.buildWebPathTo
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

object PerformWalking : LeafTask() {
    override fun execute() {
        val player = Players.getLocal() ?: return
        val bottomStaircase = GameObjects.newQuery().on(Coordinate(3448, 3539, 0)).names("Staircase").actions("Climb up").results().nearest()
        val middleStaircase = GameObjects.newQuery().on(Coordinate(3448, 3539, 1)).names("Staircase").actions("Climb up").results().nearest()
        var playerPlane = player.position?.plane
        if (playerPlane == null)
            playerPlane = 0
        if (middleStaircase != null && playerPlane == 1) {
            if (middleStaircase.interactAndTurn("Climb up")) {
                Execution.delayWhile({ middleStaircase.isValid && middleStaircase.isVisible }, { player.active() }, 2000, 3000)
            }
        } else if (bottomStaircase != null && playerPlane == 0) {
            if (bottomStaircase.interactAndTurn("Climb up")) {
                Execution.delayWhile({ bottomStaircase.isValid && bottomStaircase.isVisible }, { player.active() }, 2000, 3000)
            }
        } else {
            buildWebPathTo(Coordinate(3443, 3543, 0))?.step()
        }
    }
}