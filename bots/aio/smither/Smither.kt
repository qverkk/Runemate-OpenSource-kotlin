package com.qverkk.kotlin.bots.aio.smither

import com.qverkk.kotlin.bots.aio.smither.data.Config
import com.qverkk.kotlin.bots.aio.smither.ui.controllers.SettingsController
import com.qverkk.kotlin.framework.core.system.IdleTask
import com.qverkk.kotlin.framework.core.system.SlimTree
import com.runemate.game.api.script.framework.tree.TreeTask
import javafx.scene.Node

class Smither : SlimTree() {

    val config = Config()

    override fun onStart() {

    }

    override fun createRootTask(): TreeTask {
        return IdleTask
    }

    override val settingsPane: Node?
        get() = SettingsController(this)
}