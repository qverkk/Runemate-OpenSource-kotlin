package com.qverkk.kotlin.framework.ui.controllers

import com.qverkk.kotlin.api.resources.fxmlAttach
import com.qverkk.kotlin.framework.ui.interfaces.GeneralInterface
import com.qverkk.kotlin.framework.ui.trackers.ItemTracker
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.net.GrandExchange
import com.runemate.game.api.hybrid.util.StopWatch
import com.runemate.game.api.hybrid.util.calculations.CommonMath
import com.runemate.game.api.script.framework.AbstractBot
import com.runemate.game.api.script.framework.listeners.InventoryListener
import com.runemate.game.api.script.framework.listeners.MoneyPouchListener
import com.runemate.game.api.script.framework.listeners.SkillListener
import com.runemate.game.api.script.framework.listeners.events.ItemEvent
import com.runemate.game.api.script.framework.listeners.events.MoneyPouchEvent
import com.runemate.game.api.script.framework.listeners.events.SkillEvent
import javafx.application.Platform
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import java.awt.Desktop
import java.net.URI
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class SlimUi<T>(private val bot: T) : VBox(), Initializable, SkillListener, MoneyPouchListener, InventoryListener where T : AbstractBot, T : GeneralInterface {

    private val timer: StopWatch = StopWatch()

    @FXML
    private lateinit var botSettingsPane: Pane
    @FXML
    private lateinit var settingsImageView: ImageView
    @FXML
    private lateinit var profitImageView: ImageView
    @FXML
    private lateinit var skillImageView: ImageView
    @FXML
    private lateinit var botNameLabel: Label
    @FXML
    private lateinit var botAuthorLabel: Label
    @FXML
    private lateinit var botVersionLabel: Label
    @FXML
    private lateinit var runtimeLabel: Label
    @FXML
    private lateinit var statusLabel: Label
    @FXML
    private lateinit var botThreadHyperlink: Hyperlink
    @FXML
    private lateinit var profitTableView: TableView<ItemTracker>
    @FXML
    private lateinit var itemName: TableColumn<ItemTracker, String>
    @FXML
    private lateinit var itemAmount: TableColumn<ItemTracker, Number>
    @FXML
    private lateinit var itemProfit: TableColumn<ItemTracker, Number>
    @FXML
    private lateinit var itemProfitHr: TableColumn<ItemTracker, Number>
    @FXML
    private lateinit var amountHrCol: TableColumn<ItemTracker, Number>
    @FXML
    lateinit var skillTrackersPane: VBox

    val skillMap: HashMap<Skill, SkillController<T>> = HashMap()
    private val itemMap: HashMap<String, ItemTracker> = HashMap()
    private val observableItemTracker: ObservableList<ItemTracker> = FXCollections.observableArrayList()

    init {
        this.bot.platform.invokeLater { this.bot.eventDispatcher.addListener(this) }
        fxmlAttach(bot, this, "com/qverkk/kotlin/framework/ui/resources/SlimUi.fxml")
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        fillImages()
        val botMetaData = bot.metaData
        statusLabel.textProperty().bind(bot.status)
        botNameLabel.text = botMetaData.name
        botAuthorLabel.text = "By. ${botMetaData.author}"
        botVersionLabel.text = "Bot version: ${botMetaData.version}"
        botThreadHyperlink.onAction = openBotThread(botMetaData.discussionThread
                ?: "https://www.runemate.com/community/resources/$botMetaData./")

        profitTableView.items = observableItemTracker
        profitTableView.columns.forEach { column ->
            column.prefWidthProperty().bind(profitTableView.widthProperty().divide(profitTableView.columns.size))
        }
        itemName.setCellValueFactory { param -> param.value.item }
        itemAmount.setCellValueFactory { param -> param.value.amount }
        amountHrCol.setCellValueFactory { param -> param.value.amountHr }
        itemProfit.setCellValueFactory { param -> param.value.profit }
        itemProfitHr.setCellValueFactory { param -> param.value.profitHr }

        if (bot.settingsPane != null) {
            Platform.runLater { botSettingsPane.children.add(bot.settingsPane) }
        }
        bot.runnableThreads.add(Runnable {
            Platform.runLater {
                runtimeLabel.text = bot.platform.invokeAndWait(bot.runtime::getRuntimeAsString).toString()
            }
        })
        bot.runnableThreads.add(Runnable {
            refreshSkillTracker()
        })
    }

    private fun refreshSkillTracker() {
        if (skillMap.isNotEmpty()) {
            for (value in skillMap.values) {
                value.updateStats(null)
            }
        }
    }

    private fun fillImages() {
        val skillTrackerImage = Image("http://i.imgur.com/v1Wz3p6.png")
        val profitTrackerImage = Image("http://i.imgur.com/2zQzNpi.png")
        val settingsImage = Image("http://i.imgur.com/jRy6jeW.png")
        skillImageView.image = skillTrackerImage
        profitImageView.image = profitTrackerImage
        settingsImageView.image = settingsImage
    }

    private fun openBotThread(threadUrl: String): EventHandler<ActionEvent> = EventHandler {
        //val forumLink = URI("https://www.runemate.com/community/resources/$botId/")
        //val forumLink = URI("https://www.runemate.com/community/threads/$botId/unread")
        val forumLink = URI(threadUrl)
        Desktop.getDesktop().browse(forumLink)
    }

    override fun onExperienceGained(event: SkillEvent?) {
        val skillEvent: SkillEvent = event ?: return
        skillMap.computeIfAbsent(skillEvent.skill, {
            val skillController = SkillController(bot, skillEvent, this)
            Platform.runLater({ skillTrackersPane.children.add(skillController) })
            skillController
        }).updateStats(skillEvent)
    }

    override fun onItemAdded(event: ItemEvent?) {
        if (Bank.isOpen().not() && timer.isRunning.not()) {
            val definition = event?.item?.definition ?: return
            if (itemMap.containsKey(definition.name)) {
                updateItems(event, definition, false)
            } else {
                addItems(event, definition, false)
            }
        }
    }

    override fun onItemRemoved(event: ItemEvent?) {
        if (Bank.isOpen().not() && timer.isRunning.not()) {
            val definition = event?.item?.definition ?: return
            if (itemMap.containsKey(definition.name)) {
                updateItems(event, definition, true)
            } else {
                addItems(event, definition, true)
            }
        }
    }

    private fun updateItems(event: ItemEvent, definition: ItemDefinition, removed: Boolean) {
        val itemName = definition.name
        val tracker = itemMap[itemName] ?: return
        val runtime: Long = bot.runtime.runtime
        val quantityChange = (if (removed) -1 else 1) * event.quantityChange
        tracker.amount.set(tracker.amount.get() + quantityChange)
        tracker.profit.set(tracker.profit.get() + (quantityChange * tracker.itemPrice.get()))
        tracker.amountHr.set(CommonMath.rate(TimeUnit.HOURS, runtime, tracker.amount.doubleValue()).toInt())
        tracker.profitHr.set(CommonMath.rate(TimeUnit.HOURS, runtime, tracker.profit.doubleValue()).toInt())
        timer.reset()
        timer.stop()
    }

    private fun addItems(event: ItemEvent, definition: ItemDefinition, removed: Boolean) {
        val unnotedId = definition.unnotedId
        val isTradable = ItemDefinition.get(unnotedId)?.isTradeableOnMarket
        val geItem: GrandExchange.Item? = if (isTradable == true) GrandExchange.lookup(unnotedId) else null
        val itemPrice: Int = geItem?.price ?: 0
        val quantityChange = (if (removed) -1 else 1) * event.quantityChange
        val profit: Int = itemPrice * quantityChange
        val itemName = definition.name
        Platform.runLater {
            val newTracker = ItemTracker(
                    item = SimpleStringProperty(itemName),
                    itemPrice = SimpleIntegerProperty(itemPrice),
                    amount = SimpleIntegerProperty(quantityChange),
                    amountHr = SimpleIntegerProperty(0),
                    profit = SimpleIntegerProperty(profit),
                    profitHr = SimpleIntegerProperty(0)
            )
            itemMap[itemName] = newTracker
            observableItemTracker.add(newTracker)
        }
    }

    private fun addItems(itemName: String, itemChange: Int, profit: Int, itemPrice: Int) {
        Platform.runLater {
            val newTracker = ItemTracker(
                    item = SimpleStringProperty(itemName),
                    itemPrice = SimpleIntegerProperty(itemPrice),
                    amount = SimpleIntegerProperty(itemChange),
                    amountHr = SimpleIntegerProperty(0),
                    profit = SimpleIntegerProperty(profit),
                    profitHr = SimpleIntegerProperty(0)
            )
            itemMap[itemName] = newTracker
            observableItemTracker.add(newTracker)
        }
    }

    override fun onContentsChanged(moneyPouchEvent: MoneyPouchEvent?) {
        if (moneyPouchEvent == null) return
        if (itemMap.contains("Coins")) {
            val tracker = itemMap["Coins"] ?: return
            val amount = tracker.amount.get()
            val profit = tracker.profit.get()
            val change = moneyPouchEvent.change
            val runtime = bot.runtime.runtime
            tracker.amount.set(change + amount)
            tracker.profit.set(profit + change)
            tracker.amountHr.set(CommonMath.rate(TimeUnit.HOURS, runtime, tracker.amount.doubleValue()).toInt())
            tracker.profitHr.set(CommonMath.rate(TimeUnit.HOURS, runtime, tracker.profit.doubleValue()).toInt())
        } else {
            val amount = moneyPouchEvent.change
            addItems("Coins", amount, amount, 0)
        }
    }
}
