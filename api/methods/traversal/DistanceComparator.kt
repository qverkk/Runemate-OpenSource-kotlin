package com.qverkk.kotlin.api.methods.traversal

import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.util.calculations.Distance

class DistanceComparator : Comparator<Locatable> {

    private val cache: HashMap<Locatable, Double> = HashMap()

    override fun compare(o1: Locatable, o2: Locatable): Int {
        val delta: Double = distance(o1) - distance(o2)
        return if (delta < 0) -1 else if (delta.toInt() == 0) 0 else 1
    }

    private fun distance(locatable: Locatable): Double {
        return cache.computeIfAbsent(locatable, { Distance.to(locatable) })
    }
}