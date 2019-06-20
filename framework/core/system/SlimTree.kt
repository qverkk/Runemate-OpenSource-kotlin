package com.qverkk.kotlin.framework.core.system

import com.qverkk.kotlin.framework.ui.controllers.SlimUi
import com.qverkk.kotlin.framework.ui.interfaces.GeneralInterface
import com.runemate.game.api.client.embeddable.EmbeddableUI
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.GameEvents
import com.runemate.game.api.hybrid.region.Region
import com.runemate.game.api.hybrid.util.StopWatch
import com.runemate.game.api.script.framework.core.LoopingThread
import com.runemate.game.api.script.framework.tree.TreeBot
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import java.util.concurrent.CopyOnWriteArrayList

abstract class SlimTree : TreeBot(), GeneralInterface, EmbeddableUI {

    private val statusProperty = SimpleStringProperty("Starting bot...")
    private val stopWatch = StopWatch()
    private val runnableList: MutableList<Runnable> = CopyOnWriteArrayList<Runnable>()
    private var loopingThread: LoopingThread = LoopingThread({ runnableList.forEach(Runnable::run) }, 500)
    lateinit var ui: SlimUi<SlimTree>
    private var uiProperty: SimpleObjectProperty<SlimUi<SlimTree>>? = null


    override fun onStart(vararg args: String?) {
        embeddableUI = this
        loopingThread.start()
        runtime.start()
        if (Environment.isRS3()) Region.cacheCollisionFlags(true)
        for (event in GameEvents.RS3.values())
            event.disable()

        for (event in GameEvents.Universal.values())
            event.disable()

        for (event in GameEvents.OSRS.values())
            event.disable()

        onStart()
    }

    fun setStatus(stringStatus: String) {
        logger.debug(stringStatus)
        Platform.runLater({ status.set(stringStatus) })
    }

    abstract fun onStart()

    override fun onPause() {
        runtime.stop()
    }

    override fun onResume() {
        runtime.start()
    }

    override fun onStop() {
        runtime.stop()
    }

    override fun botInterfaceProperty(): SimpleObjectProperty<SlimUi<SlimTree>>? {
        if (uiProperty == null) {
            ui = SlimUi(this)
            uiProperty = SimpleObjectProperty(ui)
        }
        return uiProperty
    }

    override val status: StringProperty
        get() = statusProperty
    override val runtime: StopWatch
        get() = stopWatch
    override val runnableThreads: MutableList<Runnable>
        get() = runnableList
}