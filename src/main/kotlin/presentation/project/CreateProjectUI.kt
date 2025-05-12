package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.TaskState
import logic.project.CreateProjectUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService


class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val printer: Printer,
    private val promptService : PromptService,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : UiLauncher {

    override suspend fun launchUi() {
        val projectName =
            promptService.promptNonEmptyString("\nEnter project name: ")

        val stateCount =
            promptService.promptNonEmptyInt("\nEnter number of states: ")
        if (stateCount < 0) {
            printer.displayLn("\nInvalid number of states.")
            return
        }

        val taskStates = mutableListOf<TaskState>()
        for (i in 1..stateCount) {
            val stateName = promptService.promptNonEmptyString("\nEnter name for state #$i: ")
            taskStates.add(TaskState(stateName = stateName))
        }

        try {
            val newProject = Project(projectName = projectName, taskStates = taskStates)
            createProjectUseCase(newProject)
            createAuditUseCase(
                Audit(
                    createdBy = getLoggedUserUseCase().username,
                    entityType = EntityType.PROJECT,
                    entityId = newProject.projectId,
                    oldState = "",
                    newState = "Created"
                )
            )
            printer.displayLn("\nProject '$projectName' created with ${taskStates.size} state(s).")
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }
}