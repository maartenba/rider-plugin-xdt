package be.maartenballiauw.rider.xdt.actions

import be.hikage.xdt4j.XdtTransformer
import be.maartenballiauw.rider.xdt.util.XdtProjectUtil
import be.maartenballiauw.rider.xdt.util.XmlUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsShowConfirmationOption
import com.intellij.util.ui.ConfirmationDialog
import com.jetbrains.rider.projectView.actions.ProjectViewActionBase
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.nodes.ProjectModelNodeVisitor
import java.io.OutputStreamWriter

class PerformXdtTransformationAction : ProjectViewActionBase(
        "Perform transformation...",
        "Perform XDT transformation") {

    override fun getItemInternal(item: ProjectModelNode) = when {
        XdtProjectUtil.isConfigTransform(item) -> item
        else -> null
    }

    override fun updatePresentation(e: AnActionEvent, items: Array<ProjectModelNode>) {
        e.presentation.isEnabled = items.size == 1
    }

    override fun actionPerformedInternal(item: ProjectModelNode, project: Project) {
        val application = ApplicationManager.getApplication()

        // Find parent config file
        val parentConfigFile : ProjectModelNode?
        if (XdtProjectUtil.isConfigFile(item.parent)) {
            // Are we working with a nested transformation?
            parentConfigFile = item.parent!!
        } else {
            // Or a sibling?
            val configFileVisitor = object : ProjectModelNodeVisitor() {
                var configFile : ProjectModelNode? = null

                override fun visitProjectFile(node: ProjectModelNode): Result {
                    if (item != node && XdtProjectUtil.isConfigFile(node) && XdtProjectUtil.isProbablyRelated(item, node)) {
                        configFile = node
                        return Result.Stop
                    }

                    return Result.Continue
                }
            }

            configFileVisitor.visit(item.parent!!)
            parentConfigFile = configFileVisitor.configFile
        }

        // No match? Bail out.
        if (parentConfigFile == null) return

        // Match? Make sure the user knows what they are doing
        val confirmationDialog = ConfirmationDialog(project, "This will overwrite '${parentConfigFile.name}' and lose the original configuration values.\n\nDo you want to overwrite the existing file?", "Confirm overwrite", AllIcons.Toolwindows.ToolWindowProject, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION)
        confirmationDialog.show()

        if (!confirmationDialog.isOK) return

        // Load transformation and parent content
        val transformFileContents = LoadTextUtil.loadText(item.getVirtualFile()!!).toString()
        val parentConfigFileContents = LoadTextUtil.loadText(parentConfigFile.getVirtualFile()!!).toString()

        // Run transformation
        val transformer = XdtTransformer()
        val transformationResult = transformer.transform(
                XmlUtil.loadDocumentFromString(parentConfigFileContents),
                XmlUtil.loadDocumentFromString(transformFileContents))

        // Set contents
        application.runWriteAction {
            val stream = parentConfigFile.getVirtualFile()!!.getOutputStream(this)
            val writer = OutputStreamWriter(stream)
            writer.write(transformationResult.asXML())
            writer.flush()
            writer.close()
        }

        // Open in editor
        FileEditorManager.getInstance(project).openFile(parentConfigFile.getVirtualFile()!!, true)
    }
}