package com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks

import com.qverkk.kotlin.api.methods.entities.interactAndTurn
import com.qverkk.kotlin.api.methods.local.active
import com.qverkk.kotlin.api.methods.traversal.buildWebPathTo
import com.qverkk.kotlin.bots.crafting.spinner.rs3.FlaxSpinner
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

class PerformWalkingTo constructor(private val locatable: Locatable) : LeafTask() {

    private val topStairsArea = Area.Rectangular(Coordinate(3206, 3206, 2), Coordinate(3203, 3209, 2))
    private val middleStairsArea = Area.Rectangular(Coordinate(3203, 3206, 1), Coordinate(3216, 3219, 1))
    private val bottomStairsArea = Area.Rectangular(Coordinate(3203, 3206, 0), Coordinate(3216, 3219, 0))

    val bot = Environment.getBot() as FlaxSpinner

    override fun execute() {
        val me = Players.getLocal() ?: return
        if (Bank.isOpen()) Bank.close()
        if (locatable.position?.plane == 2) {
            if (middleStairsArea.contains(me)) {
                val middleStairs = GameObjects.newQuery().names("Staircase").actions("Climb-up").results().nearest()
                        ?: return
                bot.setStatus("Interacting with middle stairs")
                if (middleStairs.interactAndTurn("Climb-up")) {
                    Execution.delayWhile({ middleStairs.isValid }, { me.active() }, 2000, 3000)
                }
            } else {
                val bottomStairs = GameObjects.newQuery().names("Staircase").actions("Climb-up").on(Coordinate(3204, 3207, 0)).results().nearest()
                if (bottomStairs != null) {
                    bot.setStatus("Interacting with bottom stairs")
                    if (bottomStairs.interactAndTurn("Climb-up")) {
                        Execution.delayWhile({ bottomStairs.isValid }, { me.active() }, 2000, 3000)
                    }
                } else {
                    buildWebPathTo(bottomStairsArea.center)?.step()
                }
            }
        } else {
            val topStairs = GameObjects.newQuery().names("Staircase").actions("Climb-down").within(topStairsArea).results().nearest()
            if (topStairs != null) {
                bot.setStatus("Interacting with top stairs")
                if (topStairs.interactAndTurn("Climb-down")) {
                    Execution.delayWhile({ topStairs.isValid }, { me.active() }, 2000, 3000)
                }
            } else {
                bot.setStatus("Interacting with bottom stairs")
                val bottomStairs = GameObjects.newQuery().names("Staircase").actions("Climb-up").on(Coordinate(3204, 3207, 0)).results().nearest()
                if (bottomStairs != null) {
                    if (bottomStairs.interactAndTurn("Climb-up")) {
                        Execution.delayWhile({ bottomStairs.isValid }, { me.active() }, 2000, 3000)
                    }
                } else {
                    bot.setStatus("walking to bottom stairs")
                    buildWebPathTo(bottomStairsArea.center)?.step()
                }
            }
        }
    }
}