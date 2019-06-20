package com.qverkk.kotlin.api.methods.powers

import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.rs3.local.hud.Powers
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar
import com.runemate.game.api.script.Execution

object Prayer {

    enum class Name constructor(val names: String, val varbit: Int) {
        DEFLECT_RANGE("Deflect Missiles", 16769),
        DEFLECT_MAGIC("Deflect Magic", 16768),
        DEFLECT_MELEE("Deflect Melee", 16770),
        PROTECT_RANGE("Protect from Missiles", 16746),
        PROTECT_MAGIC("Protect from Magic", 16745),
        PROTECT_MELEE("Protect from Melee", 16744),
        SOUL_SPLIT("Soul Split", 16779);

        fun active(): Boolean {
            val varbit = Varbits.load(varbit) ?: return false
            return varbit.value > 0
        }
    }

    fun activatePreset(name: String): Boolean {
        val component = Interfaces.newQuery().containers(1430)
                .actions(name).types(InterfaceComponent.Type.CONTAINER).results().first() ?: return false
        return component.interact(name) && Execution.delayUntil({ Powers.Prayer.isQuickPraying() }, 800, 1600)
    }

    fun toggleQuickPrayers(): Boolean {
        if (ActionBar.isExpanded().not()) ActionBar.toggleExpansion()
        val praying = Powers.Prayer.isQuickPraying()
        val component = Interfaces.newQuery().containers(1430)
                .actions("Select quick curses", "Select quick prayers")
                .visible().types(InterfaceComponent.Type.CONTAINER).results().first() ?: return false
        return component.click() && Execution.delayUntil({ praying != Powers.Prayer.isQuickPraying() }, 800, 1600)
    }
}