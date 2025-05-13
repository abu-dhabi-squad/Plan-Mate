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
            projects.printWithStates(printer)
            val projectIndex =
                promptService.promptSelectionIndex("\nChoose Project: ", projects.size)

            projects[projectIndex].taskStates.forEachIndexed { index, state ->
                printer.displayLn("${index + 1}- TaskState Name: ${state.stateName}")
            }

            val stateIndex = promptService.promptSelectionIndex(
                "Choose state you want to edit: ",
                projects[projectIndex].taskStates.size
            )
            val stateNewName = promptService.promptNonEmptyString("Enter the new name of the state: ")
            editStateOfProjectUseCase(
                projects[projectIndex].projectId,
                projects[projectIndex].taskStates[stateIndex].copy(stateName = stateNewName)
            )
            printer.displayLn("\nTaskState updated successfully.")
        } catch (exception: Exception) {
            printer.displayLn("\nError: ${exception.message}")
        }
    }
}