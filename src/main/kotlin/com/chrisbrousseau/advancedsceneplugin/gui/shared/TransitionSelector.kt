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
import gui.Refreshable
import gui.utils.DefaultSourcesList
import objects.OBSState
import objects.TTransition
import themes.Theme
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*

class TransitionSelector(private val plugin: AdvancedScenePlugin): JPanel(), Refreshable {
    private val logger = Logger.getLogger(TransitionSelector::class.java.name)

    private val list: JList<String> = DefaultSourcesList()
    private val listSelector: JScrollPane = JScrollPane(list)

    /**
     * Initialization & Hooks
     */

    init {
        GUI.register(this)
        initGui()

        refreshTransitions()
    }

    override fun refreshTransitions() {
        super.refreshTransitions()

        list.setListData(OBSState.transitions.map { it.name }.toTypedArray())
        list.repaint()
    }

    /**
     * Data access & callbacks
     */

    var value: TTransition?
        get() = when(list.selectedIndex < 0 || list.selectedIndex >= OBSState.transitions.size) {
            true -> null
            else -> OBSState.transitions[list.selectedIndex]
        }
        set(transition) {
            list.setSelectedValue(transition?.name, true)
        }

    fun onSelect(callback: (TTransition?) -> Unit) {
        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                logger.info("Updating transition selection to ${list.selectedIndex}")
                callback(this.value)
            }
        }
    }

    /**
     * GUI
     */

    private fun initGui() {
        layout = BorderLayout()

        list.dragEnabled = false
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION

        listSelector.preferredSize = Dimension(listSelector.preferredSize.width, 200)
        listSelector.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

        add(Label(plugin, "label_transitions"), BorderLayout.PAGE_START)
        add(listSelector, BorderLayout.CENTER)
    }
}