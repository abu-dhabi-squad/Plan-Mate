package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates

class EditStateOfProjectUI(
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val promptService: PromptService,
    private val printer: Printer,
    private val createAuditUseCase: CreateAuditUseCase
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
            val oldState = projects[projectIndex].taskStates[stateIndex].stateName
            createAuditUseCase(
                Audit(
                    entityId = projects[projectIndex].projectId,
                    entityType = EntityType.PROJECT,
                    oldState = oldState,
                    newState = stateNewName,
                    createdBy = getLoggedUserUseCase().username
                )
            )
        } catch (exception: Exception) {
            printer.displayLn("\nError: ${exception.message}")
        }
    }
}