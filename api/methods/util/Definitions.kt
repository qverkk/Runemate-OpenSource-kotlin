package com.qverkk.kotlin.api.methods.util

import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.entities.definitions.GameObjectDefinition
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition
import com.runemate.game.api.hybrid.entities.definitions.NpcDefinition

object Definitions {
    fun getName(definition: ItemDefinition?): String {
        return definition?.name ?: ""
    }

    fun getName(definition: NpcDefinition?): String {
        return definition?.name ?: ""
    }

    fun getName(definition: GameObjectDefinition?): String {
        return definition?.name ?: ""
    }

    fun getActions(definition: GameObjectDefinition?): List<String> {
        return definition?.actions ?: emptyList()
    }

    fun getInventoryActions(definition: ItemDefinition?): List<String> {
        return definition?.inventoryActions ?: emptyList()
    }

    fun getActions(definition: NpcDefinition?): List<String> {
        return definition?.actions ?: emptyList()
    }

    fun getActions(gameObject: GameObject?): List<String> {
        if (gameObject == null || gameObject.isValid.not()) return emptyList()
        return getActions(gameObject.definition)
    }

    fun getActions(npc: Npc?): List<String> {
        if (npc == null || npc.isValid.not()) return emptyList()
        return getActions(npc.definition)
    }
}