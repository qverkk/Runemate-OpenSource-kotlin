package com.qverkk.kotlin.framework.ui.controllers

import com.qverkk.kotlin.framework.ui.interfaces.GeneralInterface
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.util.Resources
import com.runemate.game.api.hybrid.util.Time
import com.runemate.game.api.hybrid.util.calculations.CommonMath
import com.runemate.game.api.script.framework.AbstractBot
import com.runemate.game.api.script.framework.listeners.events.SkillEvent
import javafx.application.Platform
import javafx.beans.binding.NumberExpression
import javafx.beans.binding.StringBinding
import javafx.beans.binding.StringExpression
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.ToggleButton
import javafx.scene.layout.VBox
import java.io.InputStream
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

class SkillController<T>(private val bot: T, skillEvent: SkillEvent, private val slimUi: SlimUi<T>) : VBox(), Initializable where T : AbstractBot, T : GeneralInterface {

    @FXML
    private lateinit var skillNameLabel: Label
    @FXML
    private lateinit var skillProgressBar: ProgressBar
    @FXML
    private lateinit var timeTillLevelLabel: Label
    @FXML
    private lateinit var xpGainedLabel: Label
    @FXML
    private lateinit var xpPerHourLabel: Label
    @FXML
    private lateinit var currentLevelLabel: Label
    @FXML
    private lateinit var lvlsGainedLabel: Label
    @FXML
    private lateinit var removeTrackerButton: ToggleButton

    private val expGained = SimpleIntegerProperty(0)
    private val lvlsGained = SimpleIntegerProperty(0)
    private val currentLevel = SimpleIntegerProperty(0)
    private val timeTillLevel = SimpleStringProperty("")
    private val expPerHour = SimpleDoubleProperty(0.0)

    private val skillName: String = skillEvent.skill.toString()
    private val skill: Skill = skillEvent.skill
    private val startExp: Int = skillEvent.previous
    private val startLevel: Int = skillEvent.skill.baseLevel

    private var initialised = false
    private var currentExperience = 0
    private var progress = 0.0
    private var expToLevel = 0

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initialised = true
        skillNameLabel.text = skillName
        xpGainedLabel.textProperty().bind(metricFormatBinding(expGained))
        xpPerHourLabel.textProperty().bind(metricFormatBinding(expPerHour))
        lvlsGainedLabel.textProperty().bind(lvlsGained.asString())
        currentLevelLabel.textProperty().bind(currentLevel.asString())
        timeTillLevelLabel.textProperty().bind(timeTillLevel)
        removeTrackerButton.onAction = EventHandler {
            slimUi.skillMap.remove(skill)
            slimUi.skillTrackersPane.children.removeAll(this)
        }
    }

    init {
        val stream: InputStream = Resources.getAsStream(bot, "com/qverkk/kotlin/framework/ui/resources/SkillTracker.fxml")
        Platform.runLater {
            val loader = FXMLLoader()
            loader.setController(this)
            loader.setRoot(this)
            loader.load<Any>(stream)
        }
    }

    fun updateStats(skillEvent: SkillEvent?) {
        if (skillEvent != null) {
            currentExperience = skillEvent.current
            progress = ((100 - skill.experienceToNextLevelAsPercent) / 100.0)
            expToLevel = skill.experienceToNextLevel
            Platform.runLater {
                currentLevel.set(skill.baseLevel)
            }
        }
        Platform.runLater {
            skillProgressBar.progress = progress
            expGained.set(currentExperience - startExp)
            val experiencePerHour = CommonMath.rate(TimeUnit.HOURS, bot.runtime.runtime, expGained.doubleValue())
            expPerHour.set(experiencePerHour)
            val timeTillLevel: Long = ((expToLevel / (experiencePerHour + 1)) * 3600000).toLong()
            this.timeTillLevel.set(Time.format(timeTillLevel))
            lvlsGained.set(currentLevel.get() - startLevel)
            currentLevel.value = currentLevel.get()
        }
    }

    /**
     * @param numberExpression provider of the number
     * @return a StringExpression that converts a NumberExpression's value into the shortened metric format
     */
    private fun metricFormatBinding(numberExpression: NumberExpression?): StringExpression {
        return if (numberExpression == null) {
            throw NullPointerException("NumberExpression must be specified")
        } else {
            object : StringBinding() {
                init {
                    super.bind(numberExpression)
                }

                override fun dispose() {
                    super.unbind(numberExpression)
                }

                override fun computeValue(): String {
                    return metricFormat(numberExpression.longValue())
                }

                override fun getDependencies(): ObservableList<*> {
                    return FXCollections.singletonObservableList(numberExpression)
                }
            }
        }
    }

    /**
     * @param number Some number that gets shortened
     * @return shortened number as String
     */
    fun metricFormat(number: Long): String {
        var finalNumber = number
        val magnitudes = charArrayOf('k', 'M', 'G', 'T', 'P', 'E')
        val s: String

        when {
            finalNumber >= 0 -> s = ""
            finalNumber <= -9200000000000000000L -> return "-9.2E"
            else -> {
                s = "-"
                finalNumber = -finalNumber
            }
        }

        if (finalNumber < 1000)
            return s + finalNumber

        var i = 0
        while (true) {
            if (finalNumber < 10000 && finalNumber % 1000 >= 100)
                return s + finalNumber / 1000 + '.' + finalNumber % 1000 / 100 + magnitudes[i]
            finalNumber /= 1000
            if (finalNumber < 1000)
                return s + finalNumber + magnitudes[i]
            i++
        }
    }
}