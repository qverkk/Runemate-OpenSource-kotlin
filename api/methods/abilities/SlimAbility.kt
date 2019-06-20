package com.qverkk.kotlin.api.methods.abilities

import com.qverkk.kotlin.api.methods.util.loggerDebug
import com.runemate.game.api.hybrid.input.Keyboard
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.hybrid.queries.results.InterfaceComponentQueryResults
import java.awt.Color
import java.util.*
import kotlin.system.measureTimeMillis

class SlimAbility {

    private var abilityComponents: InterfaceComponentQueryResults? = null
    private val keybindList: HashMap<String, String> = HashMap()
    private val abilityContainers = arrayListOf(1430, 1670, 1671, 1672, 1673, 1674)

    private fun cacheAbilityComponents() {
        if (abilityComponents == null || abilityComponents!!.isEmpty()) {
            val time = measureTimeMillis {
                abilityComponents = Interfaces.newQuery()
                        .containers(abilityContainers)
                        .types(InterfaceComponent.Type.LABEL, InterfaceComponent.Type.SPRITE)
                        .results()
            }
            loggerDebug("Ability components cached in $time ms")
        }
    }

    private fun cacheKeybinds() {
        if (keybindList.isEmpty().not()) return
        cacheAbilityComponents()
        val abilityComponents = abilityComponents ?: return
        val time = measureTimeMillis {
            Interfaces.newQuery()
                    .provider(abilityComponents::asList)
                    .types(InterfaceComponent.Type.SPRITE)
                    .filter {
                        val name = it.name
                        name != null && name.isNotEmpty()
                    }
                    .results().forEach { a ->
                        val keybindComponent = Interfaces.newQuery()
                                .provider(abilityComponents::asList)
                                .types(InterfaceComponent.Type.LABEL)
                                .containers(a.container.index)
                                .filter { it.index == a.index + 3 }
                                .results().first()
                        if (keybindComponent != null) {
                            val abilityName = a.name
                            val keybind = keybindComponent.text
                            if (keybind != null && abilityName != null && keybind.isNotEmpty() && abilityName.isNotEmpty()) {
                                keybindList.computeIfAbsent(abilityName) { keybind }
                            }
                        }
                    }
        }
        loggerDebug("Keybind components cached in $time ms")
    }

    fun resetAbilities() {
        abilityComponents?.clear()
        keybindList.clear()
        loggerDebug("Cleared ability info.")
    }

    fun activable(vararg names: String): Boolean {
        cacheAbilityComponents()
        val abilityComponents = abilityComponents ?: return false
        return Interfaces.newQuery()
                .provider(abilityComponents::asList)
                .containers(abilityContainers)
                .types(InterfaceComponent.Type.SPRITE)
                .names(*names)
                .filter({ a ->
                    val cooldownComponent = Interfaces.newQuery()
                            .provider(abilityComponents::asList)
                            .containers(abilityContainers)
                            .types(InterfaceComponent.Type.LABEL)
                            .filter({ it.index == a.index + 4 }).results().first()
                    if (cooldownComponent != null) {
                        val text = cooldownComponent.text
                        return@filter text != null && (text.isEmpty() || accurateTime(text) < 3)
                    }
                    return@filter false
                })
                .results().isNotEmpty()
    }

    fun activable(component: InterfaceComponent): Boolean {
        cacheKeybinds()
        val abilityComponents = abilityComponents ?: return false
        val cooldownComponent = Interfaces.newQuery()
                .provider(abilityComponents::asList)
                .containers(abilityContainers)
                .types(InterfaceComponent.Type.LABEL)
                .filter({ it.index == component.index + 4 }).results().first()
        if (cooldownComponent != null) {
            val text = cooldownComponent.text
            return text != null && (text.isEmpty() || accurateTime(text) < 3)
        }
        return false
    }

    fun activate(vararg names: String): Boolean {
        cacheKeybinds()
        val abilityComponents = abilityComponents ?: return false
        val ability = Interfaces.newQuery()
                .provider(abilityComponents::asList)
                .names(*names)
                .textColors(Color(255, 255, 255))
                .types(InterfaceComponent.Type.SPRITE)
                .filter({ activable(it) }).results().first() ?: return false
        val abilityName = ability.name
        val key = keybindList[abilityName]
        loggerDebug("Activating $abilityName with keybind $key")
        return Keyboard.typeKey(key)
    }

    private fun accurateTime(abilityTime: String): Int {
        return if (abilityTime.contains("m")) abilityTime.replace("m", "").toInt() * 60 else abilityTime.toInt()
    }
}