package com.qverkk.kotlin.api.methods.util

import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.script.framework.logger.BotLogger

val getLogger: BotLogger = Environment.getLogger()

fun logStuff(level: BotLogger.Level = BotLogger.Level.INFO, message: Any) = getLogger.println(level, message)

fun loggerDebug(string: String) = getLogger.debug(string)

fun loggerFine(string: String) = getLogger.fine(string)

fun loggerServe(string: String) = getLogger.severe(string)

fun loggerWarn(string: String) = getLogger.warn(string)