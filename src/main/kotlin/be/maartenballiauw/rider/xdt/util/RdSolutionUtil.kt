package be.maartenballiauw.rider.xdt.util

import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.SolutionConfigurationManager
import com.jetbrains.rider.util.idea.getComponent

class RdSolutionUtil {
    companion object {
        fun getSolutionConfigurations(project: Project): Array<String> {
            val solutionConfigurationManager = project.getComponent<SolutionConfigurationManager>()
            return solutionConfigurationManager.solutionConfigurationsAndPlatforms
                    .map { it.configuration }
                    .toTypedArray()
        }
    }
}