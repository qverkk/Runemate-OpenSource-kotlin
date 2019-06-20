package com.qverkk.kotlin.framework.core.system

import com.runemate.game.api.script.framework.tree.TreeTask

abstract class SlimBranch : TreeTask() {

    override fun isLeaf(): Boolean = false

    override fun execute() {
    }
}

infix fun TreeTask.whenever(result: () -> Boolean) = Pair(this, result)

infix fun Pair<TreeTask, () -> Boolean>.otherwise(failure: TreeTask?) = object : SlimBranch() {
    override fun validate() = second()
    override fun successTask() = first
    override fun failureTask() = failure
}