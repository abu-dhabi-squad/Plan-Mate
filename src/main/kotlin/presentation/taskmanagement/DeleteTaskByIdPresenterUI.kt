package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase

class DeleteTaskByIdPresenterUI(
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
            printer.displayLn("Error loading projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("No projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("Select a project:", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.id)
        } catch (e: Exception) {
            printer.displayLn("Error loading tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("No tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("Select a task to delete:", tasks.size)
        val selectedTask = tasks[taskIndex]

        try {
            deleteTaskByIdUseCase(selectedTask.id)
            createAuditUseCase(
                Audit(
                    entityId = selectedTask.id.toString(),
                    entityType = EntityType.TASK,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("Task '${selectedTask.title}' deleted successfully.")
        } catch (e: Exception) {
            printer.displayLn("Failed to delete task: ${e.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("Available projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.displayLn("Tasks in selected project:")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title} (ID: ${task.id})")
        }
    }

    private fun promptSelection(prompt: String, max: Int): Int {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.displayLn("Please enter a number between 1 and $max.")
        }
    }
}
