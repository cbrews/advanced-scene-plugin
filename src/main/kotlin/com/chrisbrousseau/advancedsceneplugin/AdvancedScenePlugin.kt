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

package com.chrisbrousseau.advancedsceneplugin

import com.chrisbrousseau.advancedsceneplugin.gui.DetailPanel
import com.chrisbrousseau.advancedsceneplugin.gui.SourcePanel
import com.chrisbrousseau.advancedsceneplugin.queItems.AdvancedSceneQueItem
import objects.que.JsonQueue
import objects.que.QueItem
import plugins.common.DetailPanelBasePlugin
import plugins.common.QueItemBasePlugin
import java.awt.Color
import java.net.URL
import java.util.logging.Logger
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JComponent

@Suppress("unused")
class AdvancedScenePlugin: QueItemBasePlugin, DetailPanelBasePlugin {
    val properties: AdvancedSceneProperties = AdvancedSceneProperties()
    val logger: Logger = Logger.getLogger(AdvancedScenePlugin::class.java.name)

    override val name = properties.get("name")
    override val description = properties.get("description")
    override val version: String = properties.get("version")
    override val tabName = properties.get("label_tab", "Advanced Scenes")

    override val icon: Icon? = createImageIcon("/com/chrisbrousseau/advancedsceneplugin/icon-14.png")
    val warningIcon: Icon? = createImageIcon("/com/chrisbrousseau/advancedsceneplugin/icon-warning-14.png")
    val errorIcon: Icon? = createImageIcon("/com/chrisbrousseau/advancedsceneplugin/icon-error-14.png")

    val sourcePanelGui: SourcePanel = SourcePanel(this)
    val detailPanelGui: DetailPanel = DetailPanel(this)

    val quickAccessColor = Color(229, 238, 255)

    /**
     * Plugin hooks
     */

    override fun enable() {
        super<QueItemBasePlugin>.enable()
        super<DetailPanelBasePlugin>.enable()
    }

    override fun disable() {
        super<QueItemBasePlugin>.disable()
        super<DetailPanelBasePlugin>.disable()
    }

    override fun sourcePanel(): JComponent {
        return sourcePanelGui
    }

    override fun detailPanel(): JComponent {
        return detailPanelGui
    }

    override fun configStringToQueItem(value: String): QueItem {
        throw NotImplementedError("Cannot convert string to AdvancedSceneQueItem")
    }

    /**
     * JSON I/O
     */

    override fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem {
        return when (jsonQueueItem.className) {
            AdvancedSceneQueItem::class.java.simpleName -> AdvancedSceneQueItem.fromJson(this, jsonQueueItem)
            else -> throw IllegalArgumentException("Invalid Advanced Scene Plugin queue item: ${jsonQueueItem.className}")
        }
    }

    /**
     * Util
     */
    private fun createImageIcon(path: String): ImageIcon? {
        val imgURL: URL? = javaClass.getResource(path)
        if (imgURL != null) {
            return ImageIcon(imgURL)
        }

        logger.severe("Couldn't find imageIcon: $path")
        return null
    }
}