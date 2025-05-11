package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.project.DeleteProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer


class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val reader: InputReader,
    private val printer: Printer,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            val projects = getAllProjectsUseCase()
            if (projects.isEmpty()) {
                printer.displayLn("\nThere are no projects in the list.")
                return
            }

            projects.forEachIndexed { index, project ->
                printer.display("${index + 1}- Project Name: ${project.projectName} - States: [ ")
                project.taskStates.forEachIndexed { stateIndex, state ->
                    printer.display("${stateIndex + 1}- TaskState Name: ${state.stateName}")
                    if (stateIndex != project.taskStates.size - 1) printer.display(", ")
                }
                printer.displayLn(" ]")
            }

            val projectIndex = promptNonEmptyInt("\nChoose Project: ") - 1
            if (projectIndex !in projects.indices) {
                printer.displayLn("\nProject not found")
                return
            }
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