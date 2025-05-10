package presentation.project

import logic.project.GetProjectByIdUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class GetProjectByIdUI(
    private val inputReader: InputReader,
    private val printer: Printer,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : UiLauncher {

    override suspend fun launchUi() {
        printer.displayLn("Enter project ID:")
        val id = inputReader.readString()?.trim()
        if (id.isNullOrEmpty()) {
            printer.displayLn("Project ID cannot be empty.")
            return
        }
        getProjectById(id)
    }

    private suspend fun getProjectById(projectId: String) {
        try {
            val project = getProjectByIdUseCase(projectId)
            printer.displayLn("Project found: ${project.projectName}")
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}