package com.qverkk.kotlin.bots.aio.smither.ui.controllers

import com.qverkk.kotlin.bots.aio.smither.Smither
import com.qverkk.kotlin.bots.aio.smither.data.TrainingType
import com.qverkk.kotlin.framework.core.system.SlimTree
import com.qverkk.kotlin.framework.ui.controllers.SlimUi
import com.runemate.game.api.hybrid.util.Resources
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import java.io.IOException
import java.net.URL
import java.util.*


class SettingsController(private val bot: Smither) : Initializable, StackPane() {

    private val mainController: SlimUi<SlimTree> = bot.ui

    @FXML
    private lateinit var progressiveBtn: Button
    @FXML
    private lateinit var miningBtn: Button
    @FXML
    private lateinit var smithingBtn: Button
    @FXML
    private lateinit var smeltingBtn: Button

    @FXML
    private lateinit var miningAnchor: AnchorPane
    @FXML
    private lateinit var smithingAnchor: AnchorPane
    @FXML
    private lateinit var smeltingAnchor: AnchorPane
    @FXML
    private lateinit var progressiveAnchor: AnchorPane
    @FXML
    private lateinit var settingsAnchor: AnchorPane

    @FXML
    private lateinit var miningBackBtn: Button
    @FXML
    private lateinit var smithingBackBtn: Button
    @FXML
    private lateinit var smeltingBackBtn: Button
    @FXML
    private lateinit var progressiveBackBtn: Button

    private var currentView: AnchorPane? = null

    init {
        val loader = FXMLLoader()
        val stream = Resources.getAsStream(bot, "com/qverkk/kotlin/bots/aio/smither/ui/resources/Settings.fxml")
        loader.setController(this)
        loader.setRoot(this)
        try {
            loader.load<Any>(stream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initializeActions()
    }

    private fun initializeActions() {
        initializeBackButtons()
        initializeSettingsButtons()
    }

    private fun initializeSettingsButtons() {
        miningBtn.setOnAction {
            changeView(miningAnchor)
            bot.config.tempTrainingType = TrainingType.MINING
        }
        smithingBtn.setOnAction {
            changeView(smithingAnchor)
            bot.config.tempTrainingType = TrainingType.SMITHING
        }
        smeltingBtn.setOnAction {
            changeView(smeltingAnchor)
            bot.config.tempTrainingType = TrainingType.SMELTING
        }
        progressiveBtn.setOnAction {
            changeView(progressiveAnchor)
            bot.config.tempTrainingType = TrainingType.PROGRESSIVE
        }
    }

    private fun initializeBackButtons() {
        miningBackBtn.setOnAction {
            backButtonAction()
        }
        progressiveBackBtn.setOnAction {
            backButtonAction()
        }
        smeltingBackBtn.setOnAction {
            backButtonAction()
        }
        smithingBackBtn.setOnAction {
            backButtonAction()
        }
    }

    private fun changeView(current: AnchorPane) {
        settingsAnchor.isVisible = false
        currentView = current
        currentView?.isVisible = true
    }

    private fun backButtonAction() {
        currentView?.isVisible = false
        settingsAnchor.isVisible = true
        bot.config.tempTrainingType = null
    }
}