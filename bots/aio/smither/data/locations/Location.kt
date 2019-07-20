package com.qverkk.kotlin.bots.aio.smither.data.locations

import com.qverkk.kotlin.bots.aio.smither.data.Rock
import com.qverkk.kotlin.bots.aio.smither.settings.CustomPlayerSense
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.LocatableEntity
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.entities.definitions.GameObjectDefinition
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.player_sense.PlayerSense
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults
import com.runemate.game.api.hybrid.region.Banks
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import kotlin.random.Random


abstract class Location {

    protected lateinit var mineArea: Area
    protected lateinit var bankArea: Area
    protected lateinit var rocksCoordinates: ArrayList<Coordinate>

    var selectedRock: Rock? = null
    val depositBlackList: ArrayList<String> = ArrayList()

    abstract fun availableRocks(): Array<String>
    abstract fun getName(): String

    fun shouldBank(): Boolean = Inventory.isFull()

    fun validate(rock: GameObject): Boolean {
        val def: GameObjectDefinition? = rock.definition
        val name = def?.name ?: ""
        return name != "Rocks" && name.contains("rocks")
    }

    fun inBank() = bankArea.contains(Players.getLocal())

    fun inMine() = mineArea.contains(Players.getLocal())

    fun getNextRock(currentRock: LocatableEntity): LocatableEntity? {
        return GameObjects.newQuery().filter {

            if (validate(it)) {
                val pos = it.position
                for (rock in rocksCoordinates) {
                    if (pos?.equals(rock) == true) {
                        it != currentRock
                    }
                }
            }
            false
        }.results().nearest()
    }

    protected fun getBankers(): LocatableEntityQueryResults<LocatableEntity> {
        val banktype = PlayerSense.getAsInteger(CustomPlayerSense.Key.BANKER_PREFERENCE.playerSenseKey)
        return when {
            banktype <= 33 ->
                Banks.getLoaded { a ->
                    when (a) {
                        is Npc -> Banks.getLoadedBankers().contains(a)
                        is GameObject -> Banks.getLoadedBankChests().contains(a)
                        else -> false
                    }
                }
            banktype in 34..66 ->
                Banks.getLoaded { a ->
                    if (a is GameObject) {
                        Banks.getLoadedBankBooths().contains(a) || Banks.getLoadedBankChests().contains(a)
                    } else {
                        false
                    }
                }
            else ->
                Banks.getLoaded()
        }
    }

    fun openBank(): Boolean {
        val bankers = getBankers()
        val player = Players.getLocal() ?: return false
        if (bankers.size < 1)
            return false
        val bank = bankers.nearest() ?: return false
        if (bank.visibility <= 10) {
            Camera.turnTo(bank)
        } else {
            val clickedBank = Bank.open(bank)
            if (Camera.getPitch() <= 0.3) {
                Camera.concurrentlyTurnTo(Random.nextDouble(0.4, 0.7))
            }
            if (clickedBank) {
                return Execution.delayUntil(Bank::isOpen, player::isMoving, 1200)
            }
        }
        return false
    }

    fun deposit(): Boolean {
        return Bank.depositAllExcept {
            val def: ItemDefinition? = it.definition
            val name = def?.name ?: ""

            for (s in depositBlackList) {
                if (name.toLowerCase().contains(s)) {
                    true
                }
            }
            false
        }
    }
}
