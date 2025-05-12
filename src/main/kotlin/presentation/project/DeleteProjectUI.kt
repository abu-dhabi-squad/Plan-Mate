package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.project.DeleteProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates


class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val printer: Printer,
    private val promptService: PromptService,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            projects.printWithStates(printer)
            val projectIndex = promptService.promptSelectionIndex("\nChoose Project: ", projects.size)
            deleteProjectUseCase(projects[projectIndex].projectId)
            createAuditUseCase(
                Audit(
                    entityId = projects[projectIndex].projectId,
                    entityType = EntityType.PROJECT,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("\nProject \"${projects[projectIndex].projectName}\" has been deleted.")
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }

}