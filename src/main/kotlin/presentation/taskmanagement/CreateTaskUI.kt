package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.Audit.EntityType
import logic.model.Project
import logic.model.Task
import logic.model.TaskState
import logic.project.GetAllProjectsUseCase
import logic.task.CreateTaskUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CreateTaskUI(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val promptUtils : PromptUtils,
    private val createAuditUseCase: CreateAuditUseCase

) : UiLauncher {
    override suspend fun launchUi() {
        val title = promptUtils.promptNonEmptyString("Enter task title: ")
        val description =
            promptUtils.promptNonEmptyString("Enter task description: ")
        val startDate = promptUtils.promptDate("Enter task start date (YYYY-MM-DD): ")
        val endDate = promptUtils.promptDate("Enter task end date (YYYY-MM-DD): ")
        val projects = try {
            getAllProjectsUseCase()
        } catch (exception: Exception) {
            printer.displayLn("\nError loading projects: ${exception.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptUtils.promptSelectionIndex("\nEnter project number", projects.size)
        val selectedProject = projects[projectIndex]

        showStates(selectedProject.taskStates)
        val stateIndex = promptUtils.promptSelectionIndex("\nEnter state number", selectedProject.taskStates.size)
        val selectedState = selectedProject.taskStates[stateIndex]

        val task = Task(
            username = getLoggedUserUseCase().username,
            projectId = selectedProject.projectId,
            taskStateId = selectedState.stateId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate
        )

        try {
            createTaskUseCase(task)
            createAuditUseCase(
                Audit(
                    entityId = task.taskId,
                    entityType = EntityType.TASK,
                    oldState = "",
                    newState = selectedState.stateName,
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("\nTask created successfully.")
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to create task: ${exception.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("\nAvailable projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showStates(taskStates: List<TaskState>) {
        printer.displayLn("\nAvailable taskStates:")
        taskStates.forEachIndexed { index, state ->
            printer.displayLn("${index + 1}. ${state.stateName}")
        }
    }
}
