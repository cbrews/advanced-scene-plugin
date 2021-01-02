package com.chrisbrousseau.advancedScene

import java.util.*

class AdvancedSceneProperties {
    private val properties = Properties()

    init {
        properties.load(AdvancedSceneProperties::class.java.getResourceAsStream("/com/chrisbrousseau/advancedScene/plugin.properties"))
    }

    fun get(propertyName: String, defaultValue: String = ""): String {
        return properties.getProperty(propertyName, defaultValue)
    }
}