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
import com.chrisbrousseau.advancedScene.OBSClientHelper
import gui.list.QueListCellRenderer
import objects.OBSClient
import objects.OBSState
import objects.TScene
import objects.TTransition
import objects.que.JsonQueue
import objects.que.Que
import objects.que.SceneQueItem
import themes.Theme
import java.awt.Color
import java.awt.Graphics2D
import java.text.NumberFormat
import java.util.logging.Logger
import javax.swing.Icon
import javax.swing.JLabel

class AdvancedSceneQueItem(
    override val plugin: AdvancedScenePlugin,
    override val scene: TScene,
    val transition: TTransition,
    val transitionDuration: Int
): SceneQueItem {
    private val logger = Logger.getLogger(AdvancedSceneQueItem::class.java.name)

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

    /**
     * Plugin hooks
     */

    override fun activate() {
        // The transition duration is a global value in OBS.  Update it globally.
        OBSClientHelper.setTransitionDuration(transitionDuration) {
            // TODO: Make sure scene still transitions if duration change fails (log an error to Notifier though)
            OBSClientHelper.changeSceneWithTransition(scene, transition) {
                GUI.switchedScenes()
            }

            // Update the preview in OBS to the next scene
            OBSClientHelper.updatePreviewScene {}
        }
    }

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        super.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)

        // Mark the cell with an error if the scene can no longer be found.
        // TODO more alerting colors
        if (!validScene()) {
            if (index == Que.currentIndex()) {
                cell.background = Theme.get.NON_EXISTING_SELECTED_COLOR
            }
            cell.background = Theme.get.NON_EXISTING_COLOR
            cell.icon = plugin.errorIcon
            return
        }

        // Mark the cell with a warning if the transition can no longer be found.
        if (!validTransition()) {
            if (index == Que.currentIndex()) {
                cell.background = Theme.get.NON_EXISTING_SELECTED_COLOR
            }
            cell.background = Theme.get.NON_EXISTING_COLOR
            cell.icon = plugin.warningIcon
            return
        }
    }

    /**
     * JSON I/O
     */

    companion object {
        fun fromJson(plugin: AdvancedScenePlugin, jsonQueueItem: JsonQueue.QueueItem): AdvancedSceneQueItem {
            return AdvancedSceneQueItem(
                plugin,
                TScene(jsonQueueItem.data["scene"]),
                TTransition(jsonQueueItem.data["transition"]),
                jsonQueueItem.data["transitionDuration"]!!.toInt()
            )
        }
    }

    override fun toJson(): JsonQueue.QueueItem {
        val json = super.toJson()
        json.data["scene"] = scene.name
        json.data["transition"] = transition.name
        json.data["transitionDuration"] = transitionDuration.toString()
        return json
    }

    /**
     * Internal Helpers
     */

    private fun description(): String = "${scene.name} - ${transition.name} (${numberFormatter.format(transitionDuration)} ms)"

    private fun validScene(): Boolean = OBSState.scenes.any { it.name == scene.name }

    private fun validTransition(): Boolean = OBSState.transitions.any { it.name == transition.name }
}