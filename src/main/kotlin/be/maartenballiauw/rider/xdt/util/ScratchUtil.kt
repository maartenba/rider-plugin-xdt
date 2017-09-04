package be.maartenballiauw.rider.xdt.util

import com.intellij.ide.scratch.ScratchFileService
import com.intellij.ide.scratch.ScratchRootType
import com.intellij.lang.Language
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.io.OutputStreamWriter

class ScratchUtil {
    companion object {
        @Suppress("unused")
        fun openEditor(project: Project, scratchFile: VirtualFile?) {
            if (scratchFile != null) {
                FileEditorManager.getInstance(project).openFile(scratchFile, true)
            }
        }

        fun createScratch(project: Project, language: Language, fileName: String, contents: String?) : VirtualFile? =
                ScratchRootType.getInstance().createScratchFile(
                        project, fileName, language, contents, ScratchFileService.Option.create_if_missing)

        @Suppress("unused")
        fun createTempFile(project: Project, language: Language, fileName: String, contents: String?) : VirtualFile? {
            val file = File.createTempFile(fileName.replace(".config", ""), ".config")
            val writer: OutputStreamWriter = file.writer()

            try {
                writer.write(contents)
            }
            finally {
                writer.flush()
                writer.close()
            }

            return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)!!
        }
    }
}