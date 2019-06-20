package com.qverkk.kotlin.api.resources

import com.qverkk.kotlin.framework.ui.interfaces.GeneralInterface
import com.runemate.game.api.hybrid.util.Resources
import com.runemate.game.api.script.framework.AbstractBot
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import java.io.InputStream

fun <T> fxmlAttach(bot: T, controller: Node, fxmlFilePath: String) where T : AbstractBot, T : GeneralInterface {
    val stream: InputStream = Resources.getAsStream(bot, fxmlFilePath)
    Platform.runLater {
        val loader = FXMLLoader()
        loader.setController(controller)
        loader.setRoot(controller)
        loader.load<T>(stream)
    }
}