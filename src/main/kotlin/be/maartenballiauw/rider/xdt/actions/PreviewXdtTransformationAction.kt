package be.maartenballiauw.rider.xdt.actions

import be.hikage.xdt4j.XdtTransformer
import be.maartenballiauw.rider.xdt.util.ScratchUtil
import be.maartenballiauw.rider.xdt.util.XdtProjectUtil
import be.maartenballiauw.rider.xdt.util.XmlUtil
import com.intellij.diff.DiffDialogHints
import com.intellij.diff.DiffManagerEx
import com.intellij.diff.DiffRequestFactory
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.actions.ProjectViewActionBase
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.nodes.ProjectModelNodeVisitor

class PreviewXdtTransformationAction : ProjectViewActionBase(
        "Preview transformation...",
        "Preview XDT transformation in dialog") {

    override fun getItemInternal(item: ProjectModelNode) = when {
        XdtProjectUtil.isConfigTransform(item) -> item
        else -> null
    }

    override fun updatePresentation(e: AnActionEvent, items: Array<ProjectModelNode>) {
        e.presentation.isEnabled = items.size == 1
    }

    override fun actionPerformedInternal(item: ProjectModelNode, project: Project) {
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

        // Load transformation and parent content
        val transformFileContents = LoadTextUtil.loadText(item.getVirtualFile()!!).toString()
        val parentConfigFileContents = LoadTextUtil.loadText(parentConfigFile.getVirtualFile()!!).toString()

        // Run transformation
        val transformer = XdtTransformer()
        val transformationResult = transformer.transform(
                XmlUtil.loadDocumentFromString(parentConfigFileContents),
                XmlUtil.loadDocumentFromString(transformFileContents))

        // Create a scratch file for it
        val scratchFile = ScratchUtil.createScratch(
                project, XMLLanguage.INSTANCE, "transformed_" + item.name, transformationResult.asXML())

        if (scratchFile != null) {
            // Show it in a diff viewer
            val contentDiffRequest = DiffRequestFactory.getInstance().createFromFiles(
                    project, parentConfigFile.getVirtualFile()!!, scratchFile)

            DiffManagerEx.getInstance().showDiff(project, contentDiffRequest, DiffDialogHints.DEFAULT)
        }
    }
}