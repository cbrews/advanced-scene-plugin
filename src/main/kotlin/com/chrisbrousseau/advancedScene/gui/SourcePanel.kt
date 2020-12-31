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
import objects.TScene
import objects.TTransition
import objects.que.Que
import themes.Theme
import java.awt.Dimension
import java.awt.GridLayout
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

const val LABEL_SCENES = "Scene List"
const val LABEL_TRANSITIONS = "Transition List"
const val LABEL_DURATION = "Transition Duration"
const val LABEL_ADD_BUTTON = "Add"

class SourcePanel(private val plugin: AdvancedScenePlugin): JPanel(), Refreshable {
    private val logger = Logger.getLogger(SourcePanel::class.java.name)

    private val sceneList: JList<String> = DefaultSourcesList()
    private val transitionList: JList<String> = DefaultSourcesList()

    init {
        initGui()
        GUI.register(this)

        refreshScenes()
        refreshTransitions()
    }

    private fun initGui() {
        layout = GridLayout(4, 1)
        border = EmptyBorder(10, 10, 0, 10)

        add(JLabel(LABEL_SCENES))

        val sceneScrollPane = JScrollPane(sceneList)
        sceneScrollPane.preferredSize = Dimension(300, 200)
        sceneScrollPane.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        add(sceneScrollPane)

        add(JLabel(LABEL_TRANSITIONS))

        val transitionScrollPane = JScrollPane(transitionList)
        transitionScrollPane.preferredSize = Dimension(300, 200)
        transitionScrollPane.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        add(transitionScrollPane)

        add(JLabel(LABEL_DURATION))

        val transitionDurationSpinner = JSpinner(SpinnerNumberModel(1000, 0, Int.MAX_VALUE, 250))
        transitionDurationSpinner.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        transitionDurationSpinner.preferredSize = Dimension(transitionDurationSpinner.preferredSize.height, 10)
        add(transitionDurationSpinner)

        val addButton = JButton(LABEL_ADD_BUTTON)
        addButton.addActionListener {
            val scene = OBSState.scenes.get(sceneList.selectedIndex)
            val transition = OBSState.transitions.get(transitionList.selectedIndex)
            addQueItem(scene, transition, transitionDurationSpinner.value as Int)
        }
        add(addButton)
    }

    private fun addQueItem(scene: TScene, transition: TTransition, duration: Int) {
        val queItem = AdvancedSceneQueItem(plugin, scene, transition, duration)
        Que.add(queItem)
        GUI.refreshQueItems()
    }

    override fun refreshScenes() {
        super.refreshScenes()
        sceneList.setListData(OBSState.scenes.map { it.name }.toTypedArray())
        sceneList.repaint()
    }

    override fun refreshTransitions() {
        super.refreshTransitions()
        transitionList.setListData(OBSState.transitions.map { it.name }.toTypedArray())
        transitionList.repaint()
    }


}