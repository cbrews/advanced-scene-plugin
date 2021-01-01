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

import net.twasi.obsremotejava.callbacks.Callback
import net.twasi.obsremotejava.requests.ResponseBase
import objects.OBSClient
import objects.TScene
import objects.TTransition
import objects.que.Que
import objects.que.SceneQueItem
import java.util.logging.Logger

object OBSClientHelper {
    val logger: Logger = Logger.getLogger(OBSClientHelper::class.java.name)

    fun setTransitionDuration(transitionDuration: Int, callback: (ResponseBase) -> Unit) {
        logger.info("Setting new active transition duration to $transitionDuration ms")
        OBSClient.getController()!!.setTransitionDuration(transitionDuration) {
            callback(it)
        }
    }

    fun changeSceneWithTransition(scene: TScene, transition: TTransition, callback: (ResponseBase) -> Unit) {
        // TODO: Make sure that the scene still transitions even if the specified transition cannot be found (log a message to Notifier)
        logger.info("Setting new current scene to: ${scene.name}; with transition: ${transition.name}")
        OBSClient.getController()!!.changeSceneWithTransition(scene.name, transition.name) {
            callback(it)
        }
    }

    fun updatePreviewScene(callback: (ResponseBase) -> Unit) {
        setPreviewScene(getNextValidScene() ?: return, callback)
    }

    fun setPreviewScene(scene: TScene, callback: (ResponseBase) -> Unit) {
        logger.info("Setting new preview scene to: ${scene.name}")
        OBSClient.getController()!!.setPreviewScene(scene.name) {
            callback(it)
        }
    }

    private fun getNextValidScene(): TScene? {
        if (Que.isLastItem()) {
            return null
        }

        for (i in (Que.currentIndex() + 1)..Que.getList().size) {
            when (val nextQueItem = Que.getAt(i)) {
                is SceneQueItem -> return nextQueItem.scene
            }
        }

        return null
    }
}