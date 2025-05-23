package presentation.project

import logic.model.TaskState
import logic.project.AddStateToProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.printWithStates
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AddStateToProjectUI(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val promptUtils: PromptUtils,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            projects.printWithStates(printer)
            val projectIndex = promptUtils.promptSelectionIndex("\nChoose Project: ", projects.size)
            val stateName =
                promptUtils.promptNonEmptyString("Enter the new state name: ")
            try {
                addStateToProjectUseCase(
                    projects[projectIndex].projectId,
                    TaskState(stateName = stateName)
                )
                printer.displayLn("TaskState \"$stateName\" added to project \"${projects[projectIndex].projectName}\" successfully.")
            } catch (exception: Exception) {
                printer.displayLn("Error: ${exception.message}")
            }
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}