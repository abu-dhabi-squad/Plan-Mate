package squad.abudhabi.presentation.project

import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class GetProjectByIdUI(
    private val inputReader: InputReader,
    private val printer: Printer,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : UiLauncher {

    override fun launchUi() {
        printer.displayLn("Enter project ID:")
        val id = inputReader.readString()?.trim()
        if (id.isNullOrEmpty()) {
            printer.displayLn("Project ID cannot be empty.")
            return
        }
        getProjectById(id)
    }

    private fun getProjectById(projectId: String) {
        try {
            val project = getProjectByIdUseCase(projectId)
            printer.displayLn("Project found: ${project.projectName}")
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}