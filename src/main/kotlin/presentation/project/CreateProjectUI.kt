package presentation.project

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.Audit.EntityType
import logic.model.Project
import logic.model.TaskState
import logic.project.CreateProjectUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val printer: Printer,
    private val promptUtils: PromptUtils,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        val projectName =
            promptUtils.promptNonEmptyString("\nEnter project name: ")

        val stateCount =
            promptUtils.promptNonEmptyInt("\nEnter number of states: ")
        if (stateCount < 0) {
            printer.displayLn("\nInvalid number of states.")
            return
        }

        val taskStates = mutableListOf<TaskState>()
        for (i in 1..stateCount) {
            val stateName = promptUtils.promptNonEmptyString("\nEnter name for state #$i: ")
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
        } catch (exception: Exception) {
            printer.displayLn("\nError: ${exception.message}")
        }
    }
}