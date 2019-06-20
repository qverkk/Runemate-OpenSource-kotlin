package com.qverkk.kotlin.api.methods.local

import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.entities.Player
import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.region.Npcs
import com.runemate.game.api.hybrid.region.Players

fun localPlayer(): Player? = Players.getLocal()

fun Player.target(): Boolean = target != null

fun Player.targeting(npc: Npc): Boolean = target?.equals(npc) ?: false

fun Player.animating(): Boolean = animationId != -1

fun Player.active(): Boolean = animating() || isMoving

fun Player.targeted(): Boolean = Npcs.newQuery().actions("Attack").targeting(this).results().isNotEmpty()

fun Player.targetedBy(npc: Npc): Boolean = npc.target?.equals(this) ?: false

fun Player.within(area: Area): Boolean = area.contains(this)

fun Player.isNotUsingAbilities(): Boolean = usingAsphyxiate().not() && usingAssault().not() && usingDestroy().not() && usingRapidFire().not() && usingSnipe().not()

private fun usingRapidFire(): Boolean {
    val varbit = Varbits.load(2096) ?: return false
    return varbit.value > 0
}

private fun usingSnipe(): Boolean {
    val varbit = Varbits.load(2095) ?: return false
    return varbit.value > 0
}

private fun usingAsphyxiate(): Boolean {
    val varbit = Varbits.load(2099) ?: return false
    return varbit.value > 0
}

private fun usingAssault(): Boolean {
    val varbit = Varbits.load(2093) ?: return false
    return varbit.value > 0
}

private fun usingDestroy(): Boolean {
    val varbit = Varbits.load(2075) ?: return false
    return varbit.value > 0
}

