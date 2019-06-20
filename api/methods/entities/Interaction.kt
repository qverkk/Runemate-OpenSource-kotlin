package com.qverkk.kotlin.api.methods.entities

import com.qverkk.kotlin.api.methods.traversal.buildBresenhamTo
import com.runemate.game.api.hybrid.entities.LocatableEntity
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.hud.Menu
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import com.runemate.game.api.hybrid.util.calculations.Distance
import com.runemate.game.api.hybrid.util.calculations.Random
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar
import java.util.regex.Pattern

fun interact(entity: LocatableEntity, action: String): Boolean {
    when {
        Menu.isOpen() -> Menu.close()
        entity.visibility >= 80 -> when (entity.interact(action)) {
            true -> return true
            false -> Camera.concurrentlyTurnTo(entity, Random.nextDouble(0.4, 0.849))
        }
        Distance.to(entity) > Random.nextInt(5, 10) -> buildBresenhamTo(entity)?.step()
        else -> Camera.concurrentlyTurnTo(entity, Random.nextDouble(0.4, 0.849))
    }
    return false
}

fun LocatableEntity.interactAndTurn(action: String): Boolean {
    when {
        Menu.isOpen() -> Menu.close()
        visibility >= 80 -> when (interact(action)) {
            true -> return true
            false -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
        }
        Distance.to(this) > Random.nextInt(5, 10) && position != null -> buildBresenhamTo(position!!)?.step()
        else -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
    }
    return false
}

fun LocatableEntity.interactAndTurn(action: String, locatable: Locatable?): Boolean {
    when {
        Menu.isOpen() -> Menu.close()
        visibility >= 80 -> when (interact(action)) {
            true -> return true
            false -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
        }
        Distance.to(this) > Random.nextInt(5, 10) && locatable != null -> buildBresenhamTo(locatable)?.step()
        else -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
    }
    return false
}

fun LocatableEntity.interactAndTurn(action: Pattern): Boolean {
    when {
        Menu.isOpen() -> Menu.close()
        visibility >= 80 -> when (interact(action)) {
            true -> return true
            false -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
        }
        Distance.to(this) > Random.nextInt(5, 10) && position != null -> buildBresenhamTo(position!!)?.step()
        else -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
    }
    return false
}

fun LocatableEntity.interactAndTurn(action: String, name: Pattern): Boolean {
    when {
        Menu.isOpen() -> Menu.close()
        visibility >= 80 -> when (interact(action, name)) {
            true -> return true
            false -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
        }
        Distance.to(this) > Random.nextInt(5, 10) && position != null -> buildBresenhamTo(position!!)?.step()
        else -> Camera.concurrentlyTurnTo(this, Random.nextDouble(0.4, 0.849))
    }
    return false
}

fun SpriteItem.interact(action: String, actionBar: Boolean): Boolean {
    val itemName = this.definition?.name ?: return false
    return actionBar && ActionBar.newQuery().names(itemName).results().first()?.activate(false) ?: interact(action)
}