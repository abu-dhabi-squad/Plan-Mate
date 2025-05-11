package presentation.project

import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

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
                printer.displayLn("\nThere are no projects in the list.")
                return
            }

            projects.forEachIndexed { index, project ->
                printer.displayLn("${index + 1}- Project Name: ${project.projectName} - States: ${project.taskStates}")
            }

            val projectIndex = promptNonEmptyInt("\nChoose Project: ") - 1
            if (projectIndex !in projects.indices) {
                printer.displayLn("\nProject not found")
                return
            }

            projects[projectIndex].taskStates.forEachIndexed { index, state ->
                printer.displayLn("${index + 1}- TaskState Name: ${state.stateName}")
            }

            val stateIndex = promptNonEmptyInt("Choose state you want to edit: ") - 1
            if (stateIndex !in projects[projectIndex].taskStates.indices) {
                printer.displayLn("\nTaskState not found")
                return
            }
            val stateNewName = promptNonEmptyString("Enter the new name of the state: ")

            editStateOfProjectUseCase(
                projects[projectIndex].projectId,
                projects[projectIndex].taskStates[stateIndex].copy(stateName = stateNewName)
            )
            printer.displayLn("\nTaskState updated successfully.")
        } catch (exception: Exception) {
            printer.displayLn(exception.message ?: "An error occurred.")
        }
    }

    private fun promptNonEmptyInt(prompt: String): Int {
        while (true) {
            printer.display(prompt)
            val input = reader.readInt()
            if (input != null) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }

    private fun promptNonEmptyString(prompt: String): String {
        while (true) {
            printer.display(prompt)
            val input = reader.readString()
            if (!input.isNullOrBlank()) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }
}