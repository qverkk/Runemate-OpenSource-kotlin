package com.qverkk.kotlin.api.methods.entities

import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.rs3.local.hud.interfaces.BeastOfBurden

object Familiar {

    val avaliable: Boolean = Varbits.load(6051)?.value?.higherThan(0) ?: false

    val timeRemaining: Int = Varbits.load(6055)?.value ?: -1

    private fun Int.higherThan(int: Int): Boolean = this > int

    fun query() = BeastOfBurden.newQuery()
}