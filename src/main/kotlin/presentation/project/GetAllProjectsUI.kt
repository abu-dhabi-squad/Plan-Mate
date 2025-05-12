package presentation.project

import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer

class GetAllProjectsUI(
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,

) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("\nAll Created Projects:")
        getAllProjects()
    }

    private suspend fun getAllProjects() {
        try {
            getAllProjectsUseCase().forEachIndexed { index, project ->
                printer.displayLn("${index + 1}) ${project.projectName}")
            }
        } catch (e: Exception) {
            printer.displayLn("\nError retrieving projects: ${e.message}")
        }
    }
}