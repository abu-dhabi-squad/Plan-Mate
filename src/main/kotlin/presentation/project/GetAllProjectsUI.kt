package presentation.project

import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.ui_io.Printer

class GetAllProjectsUI(
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("All Created Projects:")
        getAllProjects()
    }

    private suspend fun getAllProjects() {
        try {
            getAllProjectsUseCase().forEachIndexed { index, project ->
                printer.displayLn("${index + 1}) ${project.projectName}")
            }
        } catch (e: Exception) {
            printer.displayLn("Error retrieving projects: ${e.message}")
        }
    }
}