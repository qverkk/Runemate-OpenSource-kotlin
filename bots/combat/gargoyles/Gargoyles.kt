package com.qverkk.kotlin.bots.combat.gargoyles

import com.qverkk.kotlin.api.methods.abilities.SlimAbility
import com.qverkk.kotlin.api.methods.local.isNotUsingAbilities
import com.qverkk.kotlin.api.methods.local.localPlayer
import com.qverkk.kotlin.api.methods.local.within
import com.qverkk.kotlin.bots.combat.gargoyles.tasks.*
import com.qverkk.kotlin.framework.core.system.SlimTree
import com.qverkk.kotlin.framework.core.system.otherwise
import com.qverkk.kotlin.framework.core.system.whenever
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GroundItems
import com.runemate.game.api.script.framework.tree.TreeTask
import javafx.scene.Node

class Gargoyles : SlimTree() {

    companion object {
        private val stackableList = arrayOf("Mort myre fungus", "Rune arrow", "Mithril bar", "Steel bar", "Adamant bar")
        private val alchableList = arrayOf("Granite maul", "Rune full helm",
                "Rune platelegs", "Dark mystic robe top",
                "Off-hand rune longsword", "Rune longsword")
        val fightingArea = Area.Rectangular(Coordinate(3433, 3565, 2), Coordinate(3450, 3540, 2))
        val ability = SlimAbility()
    }

    override fun onStart() {

    }

    override fun createRootTask(): TreeTask {
        val attackGargoyles = AttackGargoyle whenever {
            val player = localPlayer()
            player != null && player.target == null && player.isNotUsingAbilities()
        } otherwise IdleTask

        val areaCheck = PerformWalking whenever {
            localPlayer()?.within(fightingArea)?.not() ?: false
        } otherwise attackGargoyles

        val builder = Inventory.newQuery().names(*alchableList)
        val alchCheck = AlchItems(builder) whenever {
            builder.results().isNotEmpty()
        } otherwise areaCheck

        val groundBuilder = GroundItems.newQuery().within(fightingArea).names(*alchableList, *stackableList)
        return LootItems(groundBuilder) whenever {
            groundBuilder.results().isNotEmpty()
        } otherwise alchCheck
    }

    override val settingsPane: Node?
        get() = null
}