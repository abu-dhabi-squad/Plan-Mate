package presentation.project

import logic.audit.CreateAuditUseCase
import logic.project.DeleteProjectUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import logic.model.Audit
import logic.model.EntityType
import logic.user.GetLoggedUserUseCase


class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val inputReader: InputReader,
    private val printer: Printer,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
): UiLauncher {
    override suspend fun launchUi() {
        printer.display("Enter the project ID to delete: ")
        val projectId = inputReader.readString()

        if (projectId.isNullOrBlank()) {
            printer.displayLn("Project name cannot be empty.")
            return
        }

        try {
            deleteProjectUseCase(projectId)
            createAuditUseCase(
                Audit(
                    entityId = projectId,
                    entityType = EntityType.PROJECT,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("Project \"$projectId\" has been deleted.")
        } catch (e: Exception) {
            printer.displayLn("Error: ${e.message}")
        }
    }
}