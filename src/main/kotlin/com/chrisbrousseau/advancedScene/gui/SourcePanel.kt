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

package com.chrisbrousseau.advancedScene.gui

import com.chrisbrousseau.advancedScene.AdvancedScenePlugin
import com.chrisbrousseau.advancedScene.queItems.AdvancedSceneQueItem
import gui.Refreshable
import gui.utils.DefaultSourcesList
import objects.OBSState
import objects.notifications.Notifications
import objects.que.Que
import themes.Theme
import java.awt.Dimension
import java.awt.BorderLayout
import java.awt.Font
import java.awt.event.ActionEvent
import java.util.logging.Logger
import javax.management.Notification
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class SourcePanel(private val plugin: AdvancedScenePlugin): JPanel(), Refreshable {
    private val logger = Logger.getLogger(SourcePanel::class.java.name)

    private val sceneList: JList<String> = DefaultSourcesList()
    private val transitionList: JList<String> = DefaultSourcesList()
    private val transitionDuration: SpinnerNumberModel = SpinnerNumberModel(1000, 0, Int.MAX_VALUE, 250)

    private val sceneListSelector: JScrollPane = JScrollPane(sceneList)
    private val transitionListSelector: JScrollPane = JScrollPane(transitionList)
    private val transitionDurationSelector: JSpinner = JSpinner(transitionDuration)

    private val addButton: JButton = JButton(plugin.properties.get("label_add_button", "Add"))

    private val marginTop = EmptyBorder(5, 0, 0, 0)

    init {
        initGui()
        GUI.register(this)

        refreshScenes()
        refreshTransitions()
    }

    /**
     * Plugin hooks
     */

    override fun refreshScenes() {
        super.refreshScenes()

        sceneList.setListData(OBSState.scenes.map { it.name }.toTypedArray())
        sceneList.repaint()

        updateAddButton()
    }

    override fun refreshTransitions() {
        super.refreshTransitions()

        transitionList.setListData(OBSState.transitions.map { it.name }.toTypedArray())
        transitionList.repaint()

        updateAddButton()
    }

    /**
     * Handlers
     */

    private fun addQueItem() {
        try {
            validateSelection()
        } catch(e: IllegalStateException) {
            logger.info("Current selection is not valid. Not adding an item to the list.")
            Notifications.add("Tried to add an invalid selection to the Queue List, skipping.", "AdvancedScenePlugin")
            return
        }
        
        val scene = OBSState.scenes[sceneList.selectedIndex]
        val transition = OBSState.transitions[transitionList.selectedIndex]
        val transitionDuration = transitionDuration.number.toInt()

        val queItem = AdvancedSceneQueItem(plugin, scene, transition, transitionDuration)

        Que.add(queItem)
        GUI.refreshQueItems()
    }

    private fun updateAddButton() {
        try {
            validateSelection()
            addButton.isEnabled = true
        } catch (e: IllegalStateException) {
            logger.info("Disabling add button due to invalid selection.")
            addButton.isEnabled = false
        }
    }

    /**
     * Helpers
     */

    private fun validateSelection() {
        if (sceneList.selectedIndex < 0 || transitionList.selectedIndex < 0 || transitionDuration.number.toInt() < 0) {
            throw IllegalStateException("Not a valid state for adding a QueItem")
        }
    }

    /**
     * GUI Interface
     */

    private fun initGui() {
        layout = BorderLayout()
        border = EmptyBorder(10, 10, 0, 10)

        add(topPanel(), BorderLayout.PAGE_START)
        add(centerPanel(), BorderLayout.CENTER)
        add(bottomPanel(), BorderLayout.PAGE_END)
    }

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

        // Scenes Selector
        val scenePane = JPanel(BorderLayout())
        scenePane.add(labelWithMargin(plugin.properties.get("label_scenes", "Sceness")), BorderLayout.PAGE_START)
        sceneList.dragEnabled = false
        sceneList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        sceneList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                logger.info("Updating scene selection to ${sceneList.selectedIndex}")
                updateAddButton()
            }
        }
        sceneListSelector.preferredSize = Dimension(sceneListSelector.preferredSize.width, 300)
        sceneListSelector.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        scenePane.add(sceneListSelector, BorderLayout.CENTER)

        // Transition Selector
        val transitionPane = JPanel(BorderLayout())
        transitionPane.add(labelWithMargin(plugin.properties.get("label_transitions", "Transitions")), BorderLayout.PAGE_START)
        transitionList.dragEnabled = false
        transitionList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        transitionList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                logger.info("Updating transition selection to ${transitionList.selectedIndex}")
                updateAddButton()
            }
        }
        transitionListSelector.preferredSize = Dimension(transitionListSelector.preferredSize.width, 200)
        transitionListSelector.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        transitionPane.add(transitionListSelector, BorderLayout.CENTER)

        // Duration Selector
        val durationPane = JPanel(BorderLayout())
        durationPane.add(labelWithMargin(plugin.properties.get("label_duration", "Duration")), BorderLayout.PAGE_START)
        transitionDurationSelector.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        durationPane.add(transitionDurationSelector, BorderLayout.PAGE_END)

        // Build layout
        pane.layout = GroupLayout(pane)
        val layout = pane.layout as GroupLayout
        layout.autoCreateGaps = true
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(scenePane)
                        .addComponent(transitionPane)
                        .addComponent(durationPane)
                )
        )
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(scenePane)
                .addComponent(transitionPane)
                .addComponent(durationPane)
        )

        return pane
    }

    private fun bottomPanel(): JComponent {
        // Add button
        addButton.addActionListener { addQueItem() }
        return addButton
    }

    private fun labelWithMargin(labelText: String): JLabel {
        val label = JLabel(labelText)
        label.border = marginTop
        return label
    }
}