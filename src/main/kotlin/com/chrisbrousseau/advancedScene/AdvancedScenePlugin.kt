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

package com.chrisbrousseau.advancedScene

import com.chrisbrousseau.advancedScene.gui.SourcePanel
import gui.utils.createImageIcon
import objects.que.JsonQueue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import java.awt.Color
import javax.swing.Icon
import javax.swing.JComponent

@Suppress("unused")
class AdvancedScenePlugin: QueItemBasePlugin {
    override val name = "AdvancedScenePlugin"
    override val description = "Adds scene advancedScene control to OBS Scene Queue"
    override val version: String = "0.1.0"

    override val icon: Icon? = createImageIcon("icon-14.png")
    override val tabName = "Advanced Scenes"

    val quickAccessColor = Color(229, 238, 255)

    override fun sourcePanel(): JComponent {
        return SourcePanel(this)
    }

    override fun configStringToQueItem(value: String): QueItem {
        TODO("Deprecated abstract function")
    }

    override fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem {
        throw NotImplementedError("Not implemented")
    }
}