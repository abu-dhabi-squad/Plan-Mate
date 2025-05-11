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
import presentation.io.InputReader
import presentation.io.Printer

class DeleteTaskByIdUI(
    private val printer: Printer,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val createAuditUseCase: CreateAuditUseCase
) : UiLauncher {

    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.displayLn("\nError loading projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("\nSelect a project: ", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.projectId)
        } catch (e: Exception) {
            printer.displayLn("\nError loading tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("\nSelect a task to delete: ", tasks.size)
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
        } catch (e: Exception) {
            printer.displayLn("\nFailed to delete task: ${e.message}")
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

    private fun promptSelection(prompt: String, max: Int): Int {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.displayLn("\nPlease enter a number between 1 and $max.")
        }
    }
}
