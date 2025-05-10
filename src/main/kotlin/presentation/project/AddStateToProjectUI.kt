package presentation.project

import logic.model.State
import logic.project.AddStateToProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class AddStateToProjectUI(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
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
                printer.displayLn("${index + 1}- Project Name: ${project.projectName} - States: ${project.states}")
            }

            val projectIndex = promptNonEmptyInt("\nChoose Project: ") - 1
            if (projectIndex !in projects.indices) {
                printer.displayLn("\nProject not found")
                return
            }

            val stateName = promptNonEmptyString("Enter the new state name: ")

            try {
                addStateToProjectUseCase(projects[projectIndex].id.toString(), State(name = stateName))
                printer.displayLn("State \"$stateName\" added to project \"${projects[projectIndex].projectName}\" successfully.")
            } catch (e: Exception) {
                printer.displayLn("Error: ${e.message}")
            }
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