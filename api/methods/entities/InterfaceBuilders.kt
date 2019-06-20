package com.qverkk.kotlin.api.methods.entities

import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.hybrid.queries.InterfaceComponentQueryBuilder

fun InterfaceComponentQueryBuilder.findFirst() = results().first()

fun interfaceQuery() = Interfaces.newQuery()