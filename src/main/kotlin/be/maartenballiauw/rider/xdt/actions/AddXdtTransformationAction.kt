package be.maartenballiauw.rider.xdt.actions

import be.maartenballiauw.rider.xdt.dialogs.AddXdtTransformationDialog
import be.maartenballiauw.rider.xdt.util.RdProjectUtil
import be.maartenballiauw.rider.xdt.util.RdSolutionUtil
import be.maartenballiauw.rider.xdt.util.XdtProjectUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.VcsShowConfirmationOption
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.ConfirmationDialog
import com.jetbrains.rider.projectView.actions.ProjectViewActionBase
import com.jetbrains.rider.projectView.actions.addExisting.AddExistingFileAction
import com.jetbrains.rider.projectView.actions.newFile.RiderNewActionBase
import com.jetbrains.rider.projectView.nodes.ProjectModelNode
import com.jetbrains.rider.projectView.nodes.containingProject


class AddXdtTransformationAction : ProjectViewActionBase(
        "Add transformation...",
        "Add XDT transformation...") {

    override fun getItemInternal(item: ProjectModelNode) = when {
        XdtProjectUtil.isConfigFile(item) -> item
        else -> null
    }

    override fun updatePresentation(e: AnActionEvent, items: Array<ProjectModelNode>) {
        e.presentation.isEnabled = items.size == 1
    }

    override fun actionPerformedInternal(item: ProjectModelNode, project: Project) {
        val application = ApplicationManager.getApplication()
        application.saveAll()

        val solutionConfigurations = RdSolutionUtil.getSolutionConfigurations(project)

        val dialog = AddXdtTransformationDialog(solutionConfigurations)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            val fileName = item.name.replace(".config", "." + dialog.selectedValue + ".config")

            if (item.getVirtualFile()!!.parent!!.children.any { it.name == fileName }) {
                val confirmationDialog = ConfirmationDialog(project, "File '$fileName' already exists.\n\nDo you want to overwrite the existing file?", "Confirm overwrite", AllIcons.Toolwindows.ToolWindowProject, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION)
                confirmationDialog.show()

                if (!confirmationDialog.isOK) return
            }

            val createdVirtualFile = object : WriteCommandAction<VirtualFile>(project, "Xdt.AddXdtTransformationAction") {
                override fun isGlobalUndoAction(): Boolean {
                    return true
                }

                override fun shouldRecordActionForActiveDocument(): Boolean {
                    return false
                }

                override fun getUndoConfirmationPolicy(): UndoConfirmationPolicy {
                    return UndoConfirmationPolicy.REQUEST_CONFIRMATION
                }

                @Throws(Throwable::class)
                override fun run(result: Result<VirtualFile>) {
                    val file = item.getVirtualFile()!!.parent!!.findOrCreateChildData(this, fileName)

                    VfsUtil.saveText(file, "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                            "<!-- For more information on using app.config transformation visit http://go.microsoft.com/fwlink/?LinkId=125889 -->\n" +
                            "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n" +
                            "</configuration>")

                    result.setResult(file)
                }
            }.execute()

            AddExistingFileAction.execute(item.parent!!, createdVirtualFile.resultObject)
            application.invokeAndWait { RiderNewActionBase.postProcess(project, createdVirtualFile.resultObject) }

            // Make it a nested file?
            if (dialog.createNested) {
                RdProjectUtil.makeDependentUpon(application, item.containingProject(), fileName, item.name)
            }

            // Open in editor
            FileEditorManager.getInstance(project).openFile(createdVirtualFile.resultObject, true)
        }
    }
}