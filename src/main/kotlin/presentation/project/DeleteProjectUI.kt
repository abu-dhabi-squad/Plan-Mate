package presentation.project

import logic.audit.CreateAuditUseCase
import logic.project.DeleteProjectUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import logic.model.Audit
import logic.model.EntityType
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase


class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val reader: InputReader,
    private val printer: Printer,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
): UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            if (projects.isEmpty()) {
                printer.displayLn("\nThere are no projects in the list.")
                return
            }

            projects.forEachIndexed { index, project ->
                printer.displayLn("${index + 1}- Project Name: ${project.projectName} - States: ${project.states}")
            }

            val projectIndex = promptNonEmptyInt("\nChoose Project: ") - 1
            if (projectIndex !in projects.indices) {
                printer.displayLn("\nProject not found")
                return
            }
            deleteProjectUseCase(projects[projectIndex].id.toString())
            createAuditUseCase(
                Audit(
                    entityId = projects[projectIndex].id.toString(),
                    entityType = EntityType.PROJECT,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("\nProject \"${projects[projectIndex].projectName}\" has been deleted.")
        } catch (exception: Exception) {
            printer.displayLn(exception.message ?: "An error occurred.")
        }
    }

    private fun promptNonEmptyInt(prompt: String): Int {
        while (true) {
            printer.display(prompt)
            val input = reader.readInt()
            if (input != null) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }
}