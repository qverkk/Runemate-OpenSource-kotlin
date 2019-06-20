package com.qverkk.kotlin.bots.crafting.spinner.rs3

import com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks.PerformWalkingTo
import com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks.SpinFlax
import com.qverkk.kotlin.bots.crafting.spinner.rs3.tasks.WithdrawFlax
import com.qverkk.kotlin.framework.core.system.SlimTree
import com.qverkk.kotlin.framework.core.system.otherwise
import com.qverkk.kotlin.framework.core.system.whenever
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.util.calculations.Distance
import com.runemate.game.api.script.framework.tree.TreeTask
import javafx.scene.Node

class FlaxSpinner : SlimTree() {

    private val bankCoordinate = Coordinate(3208, 3218, 2)
    private val bankArea = Area.Circular(bankCoordinate, 4.toDouble())

    private val spinnerCoordinate = Coordinate(3210, 3214, 1)
    private val spinnerArea = Area.Circular(spinnerCoordinate, 4.toDouble())

    override fun onStart() {

    }

    override fun createRootTask(): TreeTask {
        val spinningWheelBuilder = GameObjects.newQuery().names("Spinning wheel").actions("Spin")
        val spinnerCheck = SpinFlax(spinningWheelBuilder) whenever {
            spinningWheelBuilder.results().isNotEmpty()
        } otherwise PerformWalkingTo(spinnerArea)
        val bankCheck = WithdrawFlax(bankCoordinate) whenever { Distance.to(bankArea) < 20 } otherwise PerformWalkingTo(bankCoordinate)
        return bankCheck whenever { Inventory.contains("Flax").not() } otherwise spinnerCheck
    }

    override val settingsPane: Node?
        get() = null
}