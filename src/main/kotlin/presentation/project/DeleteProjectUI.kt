package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.Audit.EntityType
import logic.project.DeleteProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.printWithStates
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val printer: Printer,
    private val promptUtils: PromptUtils,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            projects.printWithStates(printer)
            val projectIndex = promptUtils.promptSelectionIndex("\nChoose Project: ", projects.size)
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