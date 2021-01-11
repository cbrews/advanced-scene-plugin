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
import objects.TScene
import themes.Theme
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*

class SceneSelector(private val plugin: AdvancedScenePlugin): JPanel(), Refreshable {
    private val logger = Logger.getLogger(SceneSelector::class.java.name)

    private val list: JList<String> = DefaultSourcesList()
    private val listSelector: JScrollPane = JScrollPane(list)

    /**
     * Initialization & Hooks
     */

    init {
        GUI.register(this)
        initGui()

        refreshScenes()
    }

    override fun refreshScenes() {
        super.refreshScenes()

        list.setListData(OBSState.scenes.map { it.name }.toTypedArray())
        list.repaint()
    }

    /**
     * Data access & callbacks
     */

    var value: TScene?
        get() = when(list.selectedIndex < 0 || list.selectedIndex >= OBSState.scenes.size) {
            true -> null
            else -> OBSState.scenes[list.selectedIndex]
        }
        set(scene) {
            list.setSelectedValue(scene?.name, true)
        }

    fun onSelect(callback: (TScene?) -> Unit) {
        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                logger.info("Updating scene selection to ${list.selectedIndex}")
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

        listSelector.preferredSize = Dimension(listSelector.preferredSize.width, 300)
        listSelector.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

        add(Label(plugin, "label_scenes"), BorderLayout.PAGE_START)
        add(listSelector, BorderLayout.CENTER)
    }
}