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

package com.chrisbrousseau.advancedsceneplugin.gui

import com.chrisbrousseau.advancedsceneplugin.AdvancedScenePlugin
import com.chrisbrousseau.advancedsceneplugin.gui.shared.*
import com.chrisbrousseau.advancedsceneplugin.queItems.AdvancedSceneQueItem
import objects.notifications.Notifications
import objects.que.Que
import themes.Theme
import java.awt.BorderLayout
import java.awt.Font
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class SourcePanel(private val plugin: AdvancedScenePlugin): JPanel() {
    private val logger = Logger.getLogger(SourcePanel::class.java.name)

    /**
     * Components
     */
    private val sceneSelector: SceneSelector = SceneSelector(plugin)
    private val transitionSelector: TransitionSelector = TransitionSelector(plugin)
    private val durationSpinner: DurationSpinner = DurationSpinner(plugin)
    private val addButton: Button = Button(plugin, "label_add_button")

    init {
        layout = BorderLayout()
        border = EmptyBorder(10, 10, 0, 10)

        // Scenes Selector
        sceneSelector.onSelect {
            updateAddButton()
        }

        // Transition Selector
        transitionSelector.onSelect {
            updateAddButton()
        }

        // Save handler
        addButton.addActionListener {
            addQueItem()
        }

        add(topPanel(), BorderLayout.PAGE_START)
        add(centerPanel(), BorderLayout.CENTER)
        add(bottomPanel(), BorderLayout.PAGE_END)
    }

    /**
     * Handlers
     */

    private fun addQueItem() {
        try {
            val scene = sceneSelector.value!!
            val transition = transitionSelector.value!!
            val transitionDuration = durationSpinner.value!!

            val queItem = AdvancedSceneQueItem(plugin, scene, transition, transitionDuration)

            Que.add(queItem)
            GUI.refreshQueItems()
        } catch(e: NullPointerException) {
            logger.info("Current selection is not valid. Not adding an item to the list.")
            Notifications.add("Tried to add an invalid selection to the Queue List, skipping.", "AdvancedScenePlugin")
        }
    }

    private fun updateAddButton() {
        addButton.isEnabled = sceneSelector.value != null && transitionSelector.value != null && durationSpinner.value != null
    }

    /**
     * GUI Interface
     */

    private fun topPanel(): JComponent {
        val header = JTextArea(plugin.properties.get("label_header", "Advanced Scene"))
        header.lineWrap = true
        header.isEditable = false
        header.font = header.font.deriveFont(Font.BOLD)
        header.background = Theme.get.BACKGROUND_COLOR
        return header
    }

    private fun centerPanel(): JComponent {
        val pane = JPanel()
        pane.layout = GroupLayout(pane)
        val layout = pane.layout as GroupLayout
        layout.autoCreateGaps = true
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(sceneSelector)
                        .addComponent(transitionSelector)
                        .addComponent(durationSpinner)
                )
        )
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(sceneSelector)
                .addComponent(transitionSelector)
                .addComponent(durationSpinner)
        )

        return pane
    }

    private fun bottomPanel(): JComponent {
        // Add button
        return addButton
    }
}