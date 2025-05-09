package presentation.project

import logic.model.State
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import java.util.*

class EditStateOfProjectUI(
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val reader: InputReader,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            if (projects.isEmpty()) {
                printer.displayLn("There is no project in the list.")
                return
            }

            projects.forEach { project ->
                printer.displayLn("project id: ${project.id} - project name: ${project.projectName} - states: ${project.states}")
            }

            val projectId = promptNonEmptyString("Enter project id: ")
            val stateId = promptNonEmptyString("Enter the id of the state you want to edit: ")
            val stateNewName = promptNonEmptyString("Enter the new name of the state: ")

            editStateOfProjectUseCase(projectId, State(UUID.fromString(stateId), stateNewName))
            printer.displayLn("State updated successfully.")

        } catch (e: Exception) {
            printer.displayLn(e.message ?: "An error occurred.")
        }
    }

    private fun promptNonEmptyString(prompt: String): String {
        while (true) {
            printer.display(prompt)
            val input = reader.readString()
            if (!input.isNullOrBlank()) return input
            printer.displayLn("Input cannot be empty.")
        }
    }
}