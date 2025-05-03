package presentation.project

import logic.project.DeleteProjectUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val inputReader: InputReader,
    private val printer: Printer
): UiLauncher {
    override fun launchUi() {
        printer.display("Enter the project ID to delete: ")
        val projectId = inputReader.readString()

        if (projectId.isNullOrBlank()) {
            printer.displayLn("Project name cannot be empty.")
            return
        }

        try {
            deleteProjectUseCase(projectId)
            printer.displayLn("Project \"$projectId\" has been deleted.")
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}