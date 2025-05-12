package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.Task
import logic.model.TaskState
import logic.project.GetAllProjectsUseCase
import logic.task.CreateTaskUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService

class CreateTaskUI(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val promptService : PromptService,
    private val createAuditUseCase: CreateAuditUseCase

) : UiLauncher {
    override suspend fun launchUi() {
        val title = promptService.promptNonEmptyString("Enter task title: ")
        val description =
            promptService.promptNonEmptyString("Enter task description: ")
        val startDate = promptService.promptDate("Enter task start date (YYYY-MM-DD): ")
        val endDate = promptService.promptDate("Enter task end date (YYYY-MM-DD): ")
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
        val projectIndex = promptService.promptSelectionIndex("\nEnter project number", projects.size)
        val selectedProject = projects[projectIndex]

        showStates(selectedProject.taskStates)
        val stateIndex = promptService.promptSelectionIndex("\nEnter state number", selectedProject.taskStates.size)
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
