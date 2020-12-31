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

package com.chrisbrousseau.advancedScene.queItems

import com.chrisbrousseau.advancedScene.AdvancedScenePlugin
import gui.list.QueListCellRenderer
import objects.OBSClient
import objects.TScene
import objects.TTransition
import objects.que.QueItem
import java.awt.Color
import java.awt.Graphics2D
import java.text.NumberFormat
import javax.swing.Icon
import javax.swing.JLabel

class AdvancedSceneQueItem(
    override val plugin: AdvancedScenePlugin,
    val scene: TScene,
    val transition: TTransition,
    val transitionDuration: Int
): QueItem {
    override val name: String = scene.name
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor
    override var icon: Icon? = plugin.icon
    override fun renderText() = description()
    override fun toString() = description()
    override fun toConfigString() = description()

    private var numberFormatter: NumberFormat = NumberFormat.getInstance()

    init {
        numberFormatter.isGroupingUsed = true
    }

    override fun activate() {
        OBSClient.getController()!!.setTransitionDuration(transitionDuration) {
            TODO("Make sure scene still transitions if duration change fails")
            OBSClient.getController()!!.changeSceneWithTransition(scene.name, transition.name) {
                TODO("Make sure that the scene still transitions even if the specified transition cannot be found")
                GUI.switchedScenes()
                TODO("set preview scene next queued scene")
            }
        }
    }

    private fun description(): String {
        return "${scene.name} - ${transition.name} (${numberFormatter.format(transitionDuration)} ms)"
    }

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        super.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)
    }

    override fun listCellRendererPaintAction(g: Graphics2D, queListCellRenderer: QueListCellRenderer) {
        super.listCellRendererPaintAction(g, queListCellRenderer)
    }
}