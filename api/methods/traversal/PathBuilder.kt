package com.qverkk.kotlin.api.methods.traversal

import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.location.navigation.Path
import com.runemate.game.api.hybrid.location.navigation.Traversal
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath
import com.runemate.game.api.hybrid.location.navigation.basic.CoordinatePath
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath

fun buildTo(locatable: Locatable): Path? = RegionPath.buildTo(locatable) ?: BresenhamPath.buildTo(locatable)

fun buildBresenhamTo(locatable: Locatable): Path? = BresenhamPath.buildTo(locatable)

fun buildCoordPathTo(locatable: Locatable): CoordinatePath? = RegionPath.buildTo(locatable)
        ?: BresenhamPath.buildTo(locatable)

fun buildWebPathTo(locatable: Locatable): Path? = Traversal.getDefaultWeb().pathBuilder.buildTo(locatable)
        ?: buildTo(locatable)

fun buildPath(locatable: Locatable): Path? = buildPath(locatable, true)

fun buildPath(locatable: Locatable, considerObstacles: Boolean): Path? {
    var path: Path? = RegionPath.buildTo(locatable)

    if (locatable.position == null)
        return null
    if (!locatable.position!!.isLoaded || path == null) {
        path = Traversal.getDefaultWeb().pathBuilder.buildTo(locatable)
    }
    if (!considerObstacles && path == null) {
        path = BresenhamPath.buildTo(locatable)
    }
    return path
}

fun buildViewPortPathTo(locatable: Locatable): Path? {
    val p = buildPath(locatable, false)
    /*if (p is CoordinatePath) {
        return ViewportPath.convert(p)
    }*/
    return p
}