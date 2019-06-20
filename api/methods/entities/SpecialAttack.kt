package com.qverkk.kotlin.api.methods.entities

import com.qverkk.kotlin.api.methods.util.Definitions
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition
import com.runemate.game.api.hybrid.local.Varps
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab
import com.runemate.game.api.script.Execution

object SpecialAttack {
    val energy: Int
        get() {
            return Varps.getAt(300).value / 10
        }
    val isActivated: Boolean
        get() {
            return Varps.getAt(301).value == 1
        }
    private val button: InterfaceComponent?
        get() {
            return Interfaces.newQuery().containers(593).types(InterfaceComponent.Type.SPRITE).actions("Use Special Attack").results().first()
        }

    fun activate(): Boolean {
        return isActivated || toggle(true)
    }

    fun deactivate(): Boolean {
        return !isActivated || toggle(false)
    }

    private fun toggle(enable: Boolean): Boolean {
        if (isActivated == enable) {
            return true
        }
        if (ControlPanelTab.COMBAT_OPTIONS.open()) {
            val specialButton = button
            return (specialButton != null
                    && specialButton.isVisible
                    && specialButton.interact("Use Special Attack")
                    && Execution.delayUntil({ isActivated == enable }, 600, 1200))
        }
        return false
    }

    enum class Weapon constructor(val names: String, val percentage: Int) {
        ABYSSAL_BLUDGEON("Abyssal bludgeon", 50),
        ABYSSAL_DAGGER("Abyssal dagger", 50),
        ABYSSAL_TENTACLE("Abyssal tentacle", 50),
        ABYSSAL_WHIP("Abyssal whip", 50) {
            override fun equals(name: String): Boolean {
                return super.equals(name) || name.contains("abyssal whip")
            }
        },
        ANCIENT_MACE("Ancient mace", 100),
        ARMADYL_CROSSBOW("Armadyl crossbow", 50),
        ARMADYL_GODSWORD("Armadyl godsword", 50),
        BANDOS_GODSWORD("Bandos godsword", 65),
        BARRELCHEST_ANCHOR("Barrelchest anchor", 50),
        BONE_DAGGER("Bone dagger", 75),
        CRYSTAL_HALBERD("Crystal halberd", 50),
        DARK_BOW("Dark bow", 55),
        DARKLIGHT("Darklight", 50),
        DORGESHUUN_CROSSBOW("Dorgeshuun crossbow", 75),
        DRAGON_2H_SWORD("Dragon 2h sword", 60),
        DRAGON_AXE("Dragon axe", 100),
        DRAGON_BATTLEAXE("Dragon battleaxe", 100),
        DRAGON_CLAWS("Dragon claws", 50),
        DRAGON_DAGGER("Dragon dagger", 25),
        DRAGON_HALBERD("Dragon halberd", 50),
        DRAGON_LONGSWORD("Dragon longsword", 25),
        DRAGON_MACE("Dragon mace", 25),
        DRAGON_PICKAXE("Dragon pickaxe", 100),
        DRAGON_SCIMITAR("Dragon scimitar", 50),
        DRAGON_SPEAR("Dragon spear", 50),
        DRAGON_WARHAMMER("Dragon warhammer", 50),
        EXCALIBUR("Excalibur", 100),
        GRANITE_MAUL("Granite maul", 50),
        INFERNAL_AXE("Infernal axe", 100),
        INFERNAL_PICKAXE("Infernal pickaxe", 100),
        MAGIC_COMP_BOW("Magic comp bow", 55),
        MAGIC_LONGBOW("Magic longbow", 35),
        MAGIC_SHORTBOW("Magic shortbow", 55),
        MAGIC_SHORTBOW_I("Magic shortbow (i)", 50),
        ROD_OF_IVANDIS("Rod of ivandis", 100),
        RUNE_CLAWS("Rune claws", 25),
        RUNE_THROWNAXE("Rune thrownaxe", 10),
        SARADOMIN_GODSWORD("Saradomin godsword", 50),
        SARADOMIN_SWORD("Saradomin sword", 100),
        SARADOMINS_BLESSED_SWORD("Saradomin's blessed sword", 65),
        SEERCULL("Seercull", 100),
        STAFF_OF_THE_DEAD("Staff of the dead", 100),
        TOXIC_BLOWPIPE("Toxic blowpipe", 50),
        TOXIC_STAFF_OF_THE_DEAD("Toxic staff of the dead", 100),
        ZAMORAK_GODSWORD("Zamorak godsword", 50),
        ZAMORAKIAN_HASTA("Zamorakian hasta", 50),
        ZAMORAKIAN_SPEAR("Zamorakian spear", 50);

        open fun equals(name: String): Boolean {
            return this.name == name
        }

        companion object {
            fun isSpecialAttackWeapon(definition: ItemDefinition): Boolean {
                return isSpecialAttackWeapon(Definitions.getName(definition))
            }

            private fun isSpecialAttackWeapon(name: String): Boolean {
                return values().any { it.equals(name) }
            }
        }
    }
}