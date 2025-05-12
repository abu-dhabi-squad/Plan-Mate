package presentation.project

import logic.model.TaskState
import logic.project.AddStateToProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates

class AddStateToProjectUI(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val promptService: PromptService,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            projects.printWithStates(printer)
            val projectIndex = promptService.promptSelectionIndex("\nChoose Project: ",projects.size)
            val stateName =
                promptService.promptNonEmptyString("Enter the new state name: ")
            try {
                addStateToProjectUseCase(
                    projects[projectIndex].projectId,
                    TaskState(stateName = stateName)
                )
                printer.displayLn("TaskState \"$stateName\" added to project \"${projects[projectIndex].projectName}\" successfully.")
            } catch (e: Exception) {
                printer.displayLn("Error: ${e.message}")
            }
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}