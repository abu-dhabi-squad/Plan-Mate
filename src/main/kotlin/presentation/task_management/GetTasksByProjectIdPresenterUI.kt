package presentation.task_management

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class GetTasksByProjectIdPresenterUI(
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : UiLauncher {

    override fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.display("Failed to load projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.display("No projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("Enter project number:", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.id)
        } catch (e: Exception) {
            printer.display("Failed to load tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.display("No tasks found in '${selectedProject.projectName}'.")
            return
        }

        showTasks(tasks)
    }

    private fun showProjects(projects: List<Project>) {
        printer.display("Available Projects:")
        projects.forEachIndexed { index, project ->
            printer.display("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.display("\nTasks in Project:")
        tasks.forEachIndexed { index, task ->
            printer.display("""
                ${index + 1}. ${task.title}
                   ↳ Description: ${task.description}
                   ↳ Start: ${task.startDate}, End: ${task.endDate}
                   ↳ Assigned to: ${task.userName}
                   ↳ State ID: ${task.stateId}
            """.trimIndent())
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
}

