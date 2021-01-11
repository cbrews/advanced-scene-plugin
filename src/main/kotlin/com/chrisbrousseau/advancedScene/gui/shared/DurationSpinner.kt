/**
 * Copyright 2020 Chris Brousseau
 * This file is part of OSQ Advanced Scene Plugin.
 *
 * OSQ Advanced Scene Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OSQ Advanced Scene Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OSQ Advanced Scene Plugin.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.chrisbrousseau.advancedScene.gui.shared

import com.chrisbrousseau.advancedScene.AdvancedScenePlugin
import themes.Theme
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class DurationSpinner(private val plugin: AdvancedScenePlugin): JPanel() {
    private val logger = Logger.getLogger(DurationSpinner::class.java.name)

    private val label = Label(plugin,"label_duration")
    private val transitionDuration: SpinnerNumberModel = SpinnerNumberModel(1000, 0, Int.MAX_VALUE, 250)
    private val transitionDurationSelector: JSpinner = JSpinner(transitionDuration)


    var value: Int?
        get() = when (transitionDuration.number.toInt() >= 0) {
            true -> transitionDuration.number.toInt()
            else -> null
        }
        set(value) {
            transitionDuration.value = value
        }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        transitionDurationSelector.isEnabled = enabled
    }

    init {
        layout = BorderLayout()
        border = EmptyBorder(5, 0, 0 , 0)

        transitionDurationSelector.border = EmptyBorder(2,2,2,2)

        add(label, BorderLayout.PAGE_START)
        add(transitionDurationSelector, BorderLayout.CENTER)
    }
}