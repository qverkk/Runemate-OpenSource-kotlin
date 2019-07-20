package com.qverkk.kotlin.framework.core.system

import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.tree.LeafTask

object IdleTask : LeafTask() {
    override fun execute() {
        Execution.delay(200, 300)
    }
}