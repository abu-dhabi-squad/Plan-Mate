package presentation.project

import logic.project.EditProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates

class EditProjectUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val promptService: PromptService,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            getAllProjectsUseCase().takeIf { it.isNotEmpty() }
                ?.let { projects ->
                    projects.printWithStates(printer)
                    val projectIndex =
                        promptService.promptSelectionIndex("\nChoose project: ", projects.size)
                    val projectName =
                        promptService.promptNonEmptyString("\nEnter the new name: ")
                    editProjectUseCase(projects[projectIndex].projectId, projectName)
                    printer.displayLn("\nProject updated successfully.")
                } ?: printer.displayLn("There is no project in list")
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }
}