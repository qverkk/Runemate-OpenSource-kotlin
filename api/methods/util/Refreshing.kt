package com.qverkk.kotlin.api.methods.util

import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.util.Validatable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Snippet from @author Party
 */

class Refreshing<out T : Validatable>(private val initializer: () -> T?) : ReadOnlyProperty<Any?, T?> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (value.isNullOrInvalid()) {
            value = initializer()
            Environment.getLogger().debug("[${value?.javaClass?.simpleName} Refresh] Refreshed with $value")
        }
        return value
    }

}

fun <T : Validatable> T?.isNullOrInvalid(): Boolean = this == null || !this.isValid

fun <T : Validatable> refreshing(initializer: () -> T?): Refreshing<T> = Refreshing(initializer)