package be.maartenballiauw.rider.xdt.dialogs

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.IdeBorderFactory
import com.jetbrains.rider.protocol.IPermittedModalities
import java.awt.BorderLayout
import javax.swing.*

class AddXdtTransformationDialog(
        private val availableConfigurations: Array<String>)
    : DialogWrapper(true) {

    var selectedValue : String? = null
    var createNested: Boolean = false

    init {
        title = "Select configuration"
        init()
        IPermittedModalities.getInstance().allowPumpProtocolForComponent(this.window, this.disposable)
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel().apply {
            layout = BorderLayout()
            border = IdeBorderFactory.createEmptyBorder(3)
        }

        val label = JLabel("Select configuration:")
        label.border = IdeBorderFactory.createEmptyBorder(0, 0, 2, 2)
        panel.add(label, BorderLayout.LINE_START)

        val valueEditor = JComboBox<String>(availableConfigurations.sortedArray())
        selectedValue = valueEditor.selectedItem.toString()
        valueEditor.addActionListener { selectedValue = valueEditor.selectedItem.toString() }
        panel.add(valueEditor, BorderLayout.CENTER)

        val nestedOption = JCheckBox("Nest transformation under parent .config file?")
        nestedOption.addActionListener { createNested = nestedOption.isSelected }
        panel.add(nestedOption, BorderLayout.PAGE_END)

        return panel
    }

    override fun createActions(): Array<Action> = arrayOf(okAction, cancelAction)

    override fun doOKAction() {
        super.doOKAction()
    }

    override fun doCancelAction() {
        selectedValue = null
        super.doCancelAction()
    }
}