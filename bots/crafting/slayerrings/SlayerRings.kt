package com.qverkk.kotlin.bots.crafting.slayerrings

import com.qverkk.kotlin.bots.crafting.slayerrings.tasks.GetMaterials
import com.qverkk.kotlin.bots.crafting.slayerrings.tasks.Smelt
import com.qverkk.kotlin.framework.core.system.SlimTree
import com.qverkk.kotlin.framework.core.system.otherwise
import com.qverkk.kotlin.framework.core.system.whenever
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.script.framework.tree.TreeTask
import javafx.scene.Node

class SlayerRings : SlimTree() {

    override fun onStart() {

    }

    override fun createRootTask(): TreeTask {
        return Smelt whenever { Inventory.containsAllOf("Gold bar", "Enchanted gem") } otherwise GetMaterials
    }

    override val settingsPane: Node?
        get() = null
}