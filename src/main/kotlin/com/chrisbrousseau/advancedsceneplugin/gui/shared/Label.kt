/**
 * Copyright 2020-2021 Chris Brousseau
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

package com.chrisbrousseau.advancedsceneplugin.gui.shared

import com.chrisbrousseau.advancedsceneplugin.AdvancedScenePlugin
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

class Label(private val plugin: AdvancedScenePlugin, private val labelText: String): JLabel() {
    init {
        text = plugin.properties.get(labelText, "<Unknown Label>")
        border = EmptyBorder(5, 0, 0, 0)
    }
}