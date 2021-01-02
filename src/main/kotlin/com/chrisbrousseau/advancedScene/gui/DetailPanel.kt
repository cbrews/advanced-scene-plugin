package com.chrisbrousseau.advancedScene.gui

import com.chrisbrousseau.advancedScene.AdvancedScenePlugin
import com.chrisbrousseau.advancedScene.queItems.AdvancedSceneQueItem
import gui.Refreshable
import objects.que.QueItem
import java.awt.BorderLayout
import java.awt.Font
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder

class DetailPanel(private val plugin: AdvancedScenePlugin): JComponent(), Refreshable {
    private val logger = Logger.getLogger(DetailPanel::class.java.name)

    private var queItem: AdvancedSceneQueItem? = null

    /**
     * Components
     */

    private val detailPaneLabel = JLabel()
    private val notesField = JTextArea()

    init {
        initGui()
        GUI.register(this)
    }

    /**
     * Plugin hooks & actions
     */

    fun setActiveQueItem(activeQueItem: AdvancedSceneQueItem) {
        queItem = activeQueItem
        detailPaneLabel.text = activeQueItem.scene.name
        notesField.text = activeQueItem.notes

        isVisible = true
    }

    fun save() {
        queItem?.notes = notesField.text
    }

    /**
     * GUI Interface
     */

    private fun initGui() {
        isVisible = false
        layout = BorderLayout()
        border = EmptyBorder(10, 10, 0,10)

        // Top
        detailPaneLabel.icon = plugin.icon
        detailPaneLabel.font.deriveFont(Font.BOLD)
        add(detailPaneLabel, BorderLayout.PAGE_START)

        // Center
        val notesPane = JPanel(BorderLayout())
        val notesLabel = JLabel(plugin.properties.get("label_notes_field", "Notes"))
        notesLabel.border = EmptyBorder(5, 0, 0, 0)
        notesPane.add(notesLabel, BorderLayout.PAGE_START)

        notesField.lineWrap = true
        notesPane.add(notesField, BorderLayout.CENTER)
        add(notesPane, BorderLayout.CENTER)

        // Bottom
        val saveButton = JButton(plugin.properties.get("label_save_button", "Save"))
        saveButton.addActionListener {
            save()
        }
        add(saveButton, BorderLayout.PAGE_END)
    }
}