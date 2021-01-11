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
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder

class TextField(private val plugin: AdvancedScenePlugin, label: String): JPanel() {
    private val labelComponent: Label = Label(plugin, label)
    private val textArea: JTextArea = JTextArea()

    init {
        layout = BorderLayout()

        textArea.lineWrap = true
        textArea.preferredSize = Dimension(textArea.preferredSize.width, 150)
        textArea.border = CompoundBorder(
            BorderFactory.createLineBorder(Theme.get.BORDER_COLOR),
            EmptyBorder(2,2,2,2)
        )

        add(labelComponent, BorderLayout.PAGE_START)
        add(textArea, BorderLayout.CENTER)
    }

    var text: String
        get() = textArea.text
        set(text) {
            textArea.text = text
        }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        textArea.isEnabled = enabled

    }
}