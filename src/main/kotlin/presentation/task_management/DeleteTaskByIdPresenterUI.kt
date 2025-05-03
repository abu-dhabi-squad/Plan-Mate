package presentation.task_management

import logic.audit.CreateAuditUseCase
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class DeleteTaskByIdPresenterUI(
    private val printer: Printer,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val createAuditUseCase: CreateAuditUseCase
) : UiLauncher {

    override fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.display("Error loading projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.display("No projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("Select a project:", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.id)
        } catch (e: Exception) {
            printer.display("Error loading tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.display("No tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("Select a task to delete:", tasks.size)
        val selectedTask = tasks[taskIndex]

        try {
            deleteTaskByIdUseCase(selectedTask.id)
            createAuditUseCase(
                Audit(
                    entityId = selectedTask.id,
                    entityType = EntityType.TASK,
                    oldState = "",
                    newState = "Deleted",
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.display("Task '${selectedTask.title}' deleted successfully.")
        } catch (e: Exception) {
            printer.display("Failed to delete task: ${e.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.display("Available projects:")
        projects.forEachIndexed { index, project ->
            printer.display("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.display("Tasks in selected project:")
        tasks.forEachIndexed { index, task ->
            printer.display("${index + 1}. ${task.title} (ID: ${task.id})")
        }
    }

    private fun promptSelection(prompt: String, max: Int): Int {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.display("Please enter a number between 1 and $max.")
        }
    }
}
