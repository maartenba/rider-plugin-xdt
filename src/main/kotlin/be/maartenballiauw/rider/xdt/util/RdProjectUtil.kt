package be.maartenballiauw.rider.xdt.util

import com.intellij.openapi.application.Application
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.jetbrains.rider.model.ReloadCommand
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.util.idea.syncFromBackend
import java.io.OutputStreamWriter

class RdProjectUtil {
    companion object {
        fun makeDependentUpon(application: Application, projectModelNode: ProjectModelNode?, fileName: String, parentFileName: String) {
            application.saveAll()

            val projectFile = projectModelNode!!.getVirtualFile()!!
            val projectXml = XmlUtil.loadDocumentFromString(
                    LoadTextUtil.loadText(projectFile).toString())

            val node = projectXml.rootElement
                    .elements("ItemGroup")
                    .flatMap { it.elements("Content") }
                    .firstOrNull { it.attribute("Include").text == fileName }

            val dependentElement = node!!.addElement("DependentUpon")
            dependentElement.addText(parentFileName)

            application.runWriteAction {
                val writer = OutputStreamWriter(projectFile.getOutputStream(this))
                writer.write(projectXml.asXML())
                writer.flush()
                writer.close()
            }

            // Reload project
            val reloadCommand = ReloadCommand(listOf(projectModelNode.id))
            projectModelNode.project.solution.projectModelTasks.reloadProjects.syncFromBackend(reloadCommand, projectModelNode.project)
        }

    }
}