package com.qverkk.kotlin.bots.aio.smither.data

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.awt.Color


enum class Rock {
    CLAY("Clay", arrayOf<String>("Clay"), true, true, Color(98, 74, 42), Color(122, 100, 70)),
    COPPER("Copper", arrayOf<String>("Copper ore"), true, true, Color(74, 48, 32), Color(129, 63, 37)),
    TIN("Tin", arrayOf<String>("Tin ore"), true, true, Color(106, 106, 106), Color(114, 114, 114)),
    LIMESTONE("Limestone", arrayOf<String>("Limestone"), true, true, Color(110, 110, 110)),
    BLUERITE("Bluerite", arrayOf<String>("Bluerite ore"), true, true, Color(0, 47, 122)),
    IRON("Iron", arrayOf<String>("Iron ore"), true, true, Color(32, 17, 14)),
    SILVER("Silver", arrayOf<String>("Silver ore"), true, true, Color(141, 134, 120), Color(149, 149, 149)),
    ESSENCE("Essence", arrayOf<String>("Rune essence", "Pure essence"), true, true),
    COAL("Coal", arrayOf<String>("Coal"), true, true, Color(24, 24, 17), Color(0, 0, 0)),
    PAY_DIRT("Ore vein", arrayOf<String>("Pay-dirt"), false, false),
    SANDSTONE("Sandstone", arrayOf<String>("Sandstone (1kg)", "Sandstone (2kg)", "Sandstone (5kg)", "Sandstone (10kg)"), true, true, Color(74, 47, 11)),
    GEMS("Gems", arrayOf<String>("Uncut opal", "Uncut jade", "Uncut red topaz", "Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond"), true, true, Color(66, 0, 63)),
    GOLD("Gold", arrayOf<String>("Gold ore"), true, true, Color(129, 86, 0), Color(106, 88, 30)),
    GRANITE("Granite", arrayOf<String>("Granite (500g)", "Granite (2kg)", "Granite (5kg)"), true, true, Color(94, 66, 40)),
    MITHRIL("Mithril", arrayOf<String>("Mithril ore"), true, true, Color(47, 47, 66)),
    ADAMANTITE("Adamantite", arrayOf<String>("Adamantite ore"), true, true, Color(52, 60, 52)),
    BANE("Bane", arrayOf<String>("Bane ore"), true, false),
    LIVING_ROCK_REMAINS("Living rock remains", arrayOf<String>("Living minerals"), true, false),
    CONCENTRATED_COAL("Concentrated Coal", arrayOf<String>("Coal"), true, false),
    CONCENTRATED_GOLD("Concentrated Gold", arrayOf<String>("Gold ore"), true, false),
    RUNITE("Runite", arrayOf<String>("Runite ore"), true, true, Color(73, 98, 102)),
    SEREN("Seren", arrayOf<String>("Corrupted ore"), true, false),
    UNKNOWN("Unknown", arrayOf<String>(), false, false);

    var rockName: String
    var oreNames: Array<String>
    var rs3: Boolean = false
    var osrs: Boolean = false
    var colors: Array<Color>

    constructor(name: String, oreNames: Array<String>, rs3: Boolean, osrs: Boolean) {
        this.rockName = name
        this.oreNames = oreNames
        this.rs3 = rs3
        this.osrs = osrs
        colors = emptyArray()
    }

    constructor(name: String, oreNames: Array<String>, rs3: Boolean, osrs: Boolean, vararg colors: Color) {
        this.rockName = name
        this.oreNames = oreNames
        this.rs3 = rs3
        this.osrs = osrs
        this.colors = arrayOf(*colors)
    }

    companion object {
        fun getOres(isRS3: Boolean): MutableList<String> {
            val ores = mutableListOf<String>()
            for (i in 0 until values().size) {
                if (isRS3 && values()[i].rs3)
                    ores.add(values()[i].rockName)
                else if (!isRS3 && values()[i].osrs) ores.add(values()[i].rockName)
            }
            return ores
        }

        fun getByName(name: String): Rock {
            return valueOf(name.toUpperCase().replace(" ", "_").replace("-", "_"))
        }
    }
}