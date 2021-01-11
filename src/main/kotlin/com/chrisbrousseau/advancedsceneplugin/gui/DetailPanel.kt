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
import java.awt.BorderLayout
import java.awt.Font
import java.lang.NullPointerException
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class DetailPanel(private val plugin: AdvancedScenePlugin): JPanel() {
    private val logger = Logger.getLogger(DetailPanel::class.java.name)

    /**
     * Data
     */
    private var queItem: AdvancedSceneQueItem? = null

    /**
     * State Containers
     */
    private val activePanel = JPanel()
    private val inactivePanel = JPanel()

    /**
     * Active Components
     */
    private val detailPaneLabel = JLabel()
    private val errorMessage = Error(plugin)
    private val notesField: TextField = TextField(plugin, "label_notes")
    private val transitionSelector: TransitionSelector = TransitionSelector(plugin)
    private val durationSpinner: DurationSpinner = DurationSpinner(plugin)
    private val saveButton: Button = Button(plugin,"label_save_button")

    init {
        detailPaneLabel.icon = plugin.icon
        detailPaneLabel.font.deriveFont(Font.BOLD)
        detailPaneLabel.border = EmptyBorder(0, 0 , 4, 0)

        initGui()
        toggleActivePanel(false)

        // Save handler
        saveButton.addActionListener {
            try {
                queItem?.notes = notesField.text
                queItem?.transition = transitionSelector.value!!
                queItem?.transitionDuration = durationSpinner.value!!

                Que.save()
                GUI.refreshQueItems()
            } catch(e: NullPointerException) {
                logger.info("Update selection is not valid.  Saving was skipped.")
                Notifications.add("Couldn't update the Queue List item, skipping.", "AdvancedScenePlugin")
            }
        }
    }

    /**
     * Plugin hooks
     */

    fun deactivateQueItem(inactiveQueItem: AdvancedSceneQueItem) {
        queItem = null

        toggleActivePanel(false)
    }

    fun setActiveQueItem(activeQueItem: AdvancedSceneQueItem) {
        queItem = activeQueItem

        detailPaneLabel.text = activeQueItem.scene.name
        notesField.text = activeQueItem.notes
        transitionSelector.value = activeQueItem.transition
        durationSpinner.value = activeQueItem.transitionDuration

        errorMessage.dismiss()
        if (activeQueItem.errorScene()) {
            errorMessage.setError("error_scene_missing")
        } else if(activeQueItem.errorTransition()) {
            errorMessage.setWarn("error_transition_missing")
        }

        toggleActivePanel(true)
    }

    /**
     * GUI Interface
     */

    private fun initGui() {
        layout = BorderLayout()
        border = EmptyBorder(10, 10, 10,10)

        // Active panel view
        activePanel.layout = BorderLayout()
        activePanel.add(detailPaneLabel, BorderLayout.PAGE_START)
        val activePanelCenter = JPanel()
        activePanelCenter.layout = BoxLayout(activePanelCenter, BoxLayout.PAGE_AXIS)
        activePanelCenter.add(errorMessage)
        activePanelCenter.add(notesField)
        activePanelCenter.add(transitionSelector)
        activePanelCenter.add(durationSpinner)
        activePanel.add(activePanelCenter, BorderLayout.CENTER)
        activePanel.add(saveButton, BorderLayout.PAGE_END)
        add(activePanel, BorderLayout.CENTER)

        // Inactive panel view
        inactivePanel.layout = BorderLayout()
        inactivePanel.add(Label(plugin, "label_detail_inactive"), BorderLayout.CENTER)
        add(inactivePanel, BorderLayout.PAGE_START)
    }

    private fun toggleActivePanel(state: Boolean) {
        activePanel.isVisible = state
        inactivePanel.isVisible = !state
    }
}