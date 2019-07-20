package com.qverkk.kotlin.bots.aio.smither.data

class Config {
    var trainingType: TrainingType? = TrainingType.PROGRESSIVE
    var tempTrainingType: TrainingType? = null
    var started: Boolean = false

    fun resetSettings() {

    }
}