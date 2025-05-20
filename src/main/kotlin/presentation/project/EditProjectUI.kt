package presentation.project

import logic.project.EditProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.printWithStates
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class EditProjectUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val promptUtils: PromptUtils,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            getAllProjectsUseCase().takeIf { it.isNotEmpty() }
                ?.let { projects ->
                    projects.printWithStates(printer)
                    val projectIndex = promptUtils.promptSelectionIndex("\nChoose project: ", projects.size)
                    val projectName = promptUtils.promptNonEmptyString("\nEnter the new name: ")
                    editProjectUseCase(projects[projectIndex].projectId, projectName)
                    printer.displayLn("\nProject updated successfully.")
                } ?: printer.displayLn("There is no project in list")
        } catch (exception: Exception) {
            printer.displayLn("\nError: ${exception.message}")
        }
    }
}