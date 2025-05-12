package presentation.project

import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates

class EditStateOfProjectUI(
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val promptService: PromptService,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            if (projects.isEmpty()) {
                printer.displayLn("\nThere are no projects in the list.")
                return
            }
            projects.printWithStates(printer)
            val projectIndex =
                promptService.promptNonEmptyInt("\nChoose Project: ") - 1
            if (projectIndex !in projects.indices) {
                printer.displayLn("\nProject not found")
                return
            }

            projects[projectIndex].taskStates.forEachIndexed { index, state ->
                printer.displayLn("${index + 1}- TaskState Name: ${state.stateName}")
            }

            val stateIndex = promptService.promptNonEmptyInt("Choose state you want to edit: ") - 1
            if (stateIndex !in projects[projectIndex].taskStates.indices) {
                printer.displayLn("\nTaskState not found")
                return
            }
            val stateNewName =
                promptService.promptNonEmptyString("Enter the new name of the state: ")
            editStateOfProjectUseCase(
                projects[projectIndex].projectId,
                projects[projectIndex].taskStates[stateIndex].copy(stateName = stateNewName)
            )
            printer.displayLn("\nTaskState updated successfully.")
        } catch (exception: Exception) {
            printer.displayLn(exception.message ?: "An error occurred.")
        }
    }


}