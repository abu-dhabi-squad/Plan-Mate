package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.Task
import logic.model.TaskState
import logic.project.GetAllProjectsUseCase
import logic.task.EditTaskUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptService

class EditTaskUI(
    private val printer: Printer,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val promptService: PromptService,
    private val createAuditUseCase: CreateAuditUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to load projects: ${exception.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptService.promptSelectionIndex("\nSelect a project", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.projectId)
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to load tasks: ${exception.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptService.promptSelectionIndex("\nSelect a task to edit", tasks.size)
        val selectedTask = tasks[taskIndex]

        printer.displayLn("\nEditing Task: ${selectedTask.title}")
        val newTitle =
            promptService.promptString("\nEnter new title or leave blank: ", selectedTask.title)
        val newDescription = promptService.promptString(
            "\nEnter new description or leave blank: ",
            selectedTask.description
        )
        val newStartDate =
            promptService.promptDate(
                "\nEnter new start date (YYYY-MM-DD) or leave blank: ",
                selectedTask.startDate
            )
        val newEndDate =
            promptService.promptDate("\nEnter new end date (YYYY-MM-DD) or leave blank: ", selectedTask.endDate)

        showStates(selectedProject.taskStates)
        val stateIndex = promptService.promptSelectionIndex(
            "\nSelect new state",
            selectedProject.taskStates.size,
            selectedProject.taskStates.indexOfFirst { it.stateId == selectedTask.taskStateId }
        )
        val newState = selectedProject.taskStates[stateIndex]

        val updatedTask = selectedTask.copy(
            title = newTitle,
            description = newDescription,
            startDate = newStartDate,
            endDate = newEndDate,
            taskStateId = newState.stateId
        )

        try {
            editTaskUseCase(updatedTask)
            try {
                val oldState = selectedProject.taskStates.first { it.stateId == selectedTask.taskStateId }
                createAuditUseCase(
                    Audit(
                        entityId = updatedTask.taskId,
                        entityType = EntityType.TASK,
                        oldState = oldState.stateName,
                        newState = newState.stateName,
                        createdBy = getLoggedUserUseCase().username
                    )
                )
            } catch (exception: Exception) {
                printer.displayLn("\nFailed to create audit: ${exception.message}")
            }
            printer.displayLn("\nTask updated successfully.")
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to update task: ${exception.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("\nAvailable Projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.displayLn("\nTasks in Selected Project:")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title}")
        }
    }

    private fun showStates(taskStates: List<TaskState>) {
        printer.displayLn("\nAvailable States:")
        taskStates.forEachIndexed { index, state ->
            printer.displayLn("${index + 1}. ${state.stateName}")
        }
    }
}
