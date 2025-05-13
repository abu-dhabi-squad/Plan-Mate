package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.user.GetLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptService

class DeleteTaskByIdUI(
    private val printer: Printer,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val createAuditUseCase: CreateAuditUseCase,
    private val promptService: PromptService
) : UiLauncher {

    override suspend fun launchUi() {
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
        val projectIndex = promptService.promptSelectionIndex("\nSelect a project", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.projectId)
        } catch (exception: Exception) {
            printer.displayLn("\nError loading tasks: ${exception.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex =
            promptService.promptSelectionIndex("\nSelect a task to delete", tasks.size)
        val selectedTask = tasks[taskIndex]

        try {
            deleteTaskByIdUseCase(selectedTask.taskId)
            createAuditUseCase(
                Audit(
                    entityId = selectedTask.taskId,
                    entityType = EntityType.TASK,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("\nTask '${selectedTask.title}' deleted successfully.")
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to delete task: ${exception.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("\nAvailable projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.displayLn("\nTasks in selected project:")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title}")
        }
    }

}
