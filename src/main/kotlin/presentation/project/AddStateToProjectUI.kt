package presentation.project

import logic.model.State
import logic.project.AddStateToProjectUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class AddStateToProjectUI(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
    private val inputReader: InputReader,
    private val printer: Printer
): UiLauncher {
    override suspend fun launchUi() {
        printer.display("Enter the project ID to add a state to: ")
        val projectId = inputReader.readString()

        if (projectId.isNullOrBlank()) {
            printer.displayLn("Project ID cannot be empty.")
            return
        }

        printer.display("Enter the new state name: ")
        val stateName = inputReader.readString()

        if (stateName.isNullOrBlank()) {
            printer.displayLn("State name cannot be empty.")
            return
        }

        try {
            addStateToProjectUseCase(projectId, State(name =stateName))
            printer.displayLn("State \"$stateName\" added to project \"$projectId\" successfully.")
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}