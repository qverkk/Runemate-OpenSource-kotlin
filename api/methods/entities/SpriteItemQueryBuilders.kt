package com.qverkk.kotlin.api.methods.entities

import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import com.runemate.game.api.hybrid.queries.SpriteItemQueryBuilder
import java.util.*

fun SpriteItemQueryBuilder.contains(itemName: String): SpriteItemQueryBuilder = filter { Objects.equals(it.definition?.name, itemName) }

fun SpriteItemQueryBuilder.findFirst(): SpriteItem? = results().first()