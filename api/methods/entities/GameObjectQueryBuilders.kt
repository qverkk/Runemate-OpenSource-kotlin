package com.qverkk.kotlin.api.methods.entities

import com.runemate.game.api.hybrid.queries.GameObjectQueryBuilder
import com.runemate.game.api.hybrid.region.GameObjects

fun GameObjectQueryBuilder.findNearest() = results().nearest()

fun objectQuery() = GameObjects.newQuery()

fun queryOf(query: GameObjectQueryBuilder): GameObjectQueryBuilder = query.clone()