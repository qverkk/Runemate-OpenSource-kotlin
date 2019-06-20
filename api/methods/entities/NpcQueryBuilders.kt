package com.qverkk.kotlin.api.methods.entities

import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.entities.Player
import com.runemate.game.api.hybrid.queries.NpcQueryBuilder
import com.runemate.game.api.hybrid.region.Npcs
import java.util.*

fun NpcQueryBuilder.attacking(player: Player): NpcQueryBuilder = filter { Objects.equals(it.target, player) }

fun NpcQueryBuilder.findNearest(): Npc? = results().nearest()

fun npcQuery(): NpcQueryBuilder = Npcs.newQuery()

fun queryOf(query: NpcQueryBuilder): NpcQueryBuilder = query.clone()
