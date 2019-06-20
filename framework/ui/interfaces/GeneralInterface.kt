package com.qverkk.kotlin.framework.ui.interfaces

import com.runemate.game.api.hybrid.util.StopWatch
import javafx.beans.property.StringProperty
import javafx.scene.Node

interface GeneralInterface {
    val status: StringProperty
    val runtime: StopWatch
    val settingsPane: Node?
    val runnableThreads: MutableList<Runnable>
}