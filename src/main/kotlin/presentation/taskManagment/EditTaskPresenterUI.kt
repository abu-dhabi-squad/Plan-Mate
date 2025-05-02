package squad.abudhabi.presentation.taskManagment

import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.EditTaskUseCase
import squad.abudhabi.logic.task.GetTasksByProjectIdUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class EditTaskPresenterUI(
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val editTaskUseCase: EditTaskUseCase
) : UiLauncher {

    override fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.display("❌ Failed to load projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.display("⚠️ No projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("Select a project:", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.id)
        } catch (e: Exception) {
            printer.display("❌ Failed to load tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.display("⚠️ No tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("Select a task to edit:", tasks.size)
        val selectedTask = tasks[taskIndex]

        printer.display("Editing Task: ${selectedTask.title}")
        val newTitle = promptString("Enter new title (leave blank to keep current):", selectedTask.title)
        val newDescription = promptString("Enter new description (leave blank to keep current):", selectedTask.description)

        val updatedTask = selectedTask.copy(
            title = newTitle,
            description = newDescription
        )

        try {
            editTaskUseCase(updatedTask)
            printer.display("✅ Task updated successfully.")
        } catch (e: Exception) {
            printer.display("❌ Failed to update task: ${e.message}")
        }
    }

    private fun showProjects(projects: List<squad.abudhabi.logic.model.Project>) {
        printer.display("📋 Available Projects:")
        projects.forEachIndexed { index, project ->
            printer.display("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.display("🗂️ Tasks in Selected Project:")
        tasks.forEachIndexed { index, task ->
            printer.display("${index + 1}. ${task.title} (ID: ${task.id})")
        }
    }

    private fun promptSelection(message: String, max: Int): Int {
        while (true) {
            printer.display(message)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.display("Please enter a valid number between 1 and $max.")
        }
    }

    private fun promptString(message: String, currentValue: String): String {
        printer.display(message)
        val input = inputReader.readString()
        return if (input.isNullOrBlank()) currentValue else input
    }
}
