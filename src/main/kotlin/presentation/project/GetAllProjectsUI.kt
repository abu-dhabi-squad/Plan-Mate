package presentation.project

import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.extensions.printWithStates

class GetAllProjectsUI(
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("\nAll Created Projects:")
        showAllProjects()
    }
    private suspend fun showAllProjects() {
        try {
            getAllProjectsUseCase().printWithStates(printer)
        } catch (exception: Exception) {
            printer.displayLn("\nError retrieving projects: ${exception.message}")
        }
    }
}