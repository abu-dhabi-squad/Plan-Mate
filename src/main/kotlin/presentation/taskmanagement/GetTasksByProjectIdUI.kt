package presentation.taskmanagement

import logic.model.Project
import logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class GetTasksByProjectIdUI(
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : UiLauncher {

    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.displayLn("\nFailed to load projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }

        showProjects(projects)
        val projectIndex = promptSelection("\nEnter project number: ", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.projectId)
        } catch (e: Exception) {
            printer.displayLn("\nFailed to load tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in '${selectedProject.projectName}'.")
            return
        }

        showTasks(tasks)
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("\nAvailable Projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.displayLn("\nTasks in Project:")
        tasks.forEachIndexed { index, task ->
            printer.displayLn(
                """
                ${index + 1}. ${task.title}
                   ↳ Description: ${task.description}
                   ↳ Start: ${task.startDate}, End: ${task.endDate}
                   ↳ Assigned to: ${task.username}
                   ↳ TaskState ID: ${task.taskStateId}
            """.trimIndent()
            )
        }
        printer.displayLn()
    }

    private fun promptSelection(message: String, max: Int): Int {
        while (true) {
            printer.display(message)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.displayLn("\nPlease enter a valid number between 1 and $max.")
        }
    }
}

