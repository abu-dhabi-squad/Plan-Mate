package presentation.taskmanagement

import logic.audit.CreateAuditUseCase
import logic.validation.DateParser
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.State
import logic.model.Task
import logic.project.GetAllProjectsUseCase
import logic.task.EditTaskUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase
import java.time.LocalDate

class EditTaskUI(
    private val printer: Printer,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val dateParser: DateParser,
    private val createAuditUseCase: CreateAuditUseCase
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
        val projectIndex = promptSelection("\nSelect a project: ", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.id)
        } catch (e: Exception) {
            printer.displayLn("\nFailed to load tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("\nSelect a task to edit: ", tasks.size)
        val selectedTask = tasks[taskIndex]

        printer.displayLn("\nEditing Task: ${selectedTask.title}")
        val newTitle =
            promptString("\nEnter new title or leave blank: ", selectedTask.title)
        val newDescription = promptString(
            "\nEnter new description or leave blank: ",
            selectedTask.description
        )
        val newStartDate =
            promptDate("\nEnter new start date (YYYY-MM-DD) or leave blank: ", selectedTask.startDate)
        val newEndDate =
            promptDate("\nEnter new end date (YYYY-MM-DD) or leave blank: ", selectedTask.endDate)

        showStates(selectedProject.states)
        val stateIndex = promptSelection("\nSelect new state: ", selectedProject.states.size)
        val newState = selectedProject.states[stateIndex]

        val updatedTask = selectedTask.copy(
            title = newTitle,
            description = newDescription,
            startDate = newStartDate,
            endDate = newEndDate,
            stateId = newState.id
        )

        try {
            editTaskUseCase(updatedTask)
            val oldState = selectedProject.states.first { it.id == selectedTask.stateId }

            createAuditUseCase(
                Audit(
                    entityId = updatedTask.id.toString(),
                    entityType = EntityType.TASK,
                    oldState = oldState.id.toString(),
                    newState = newState.id.toString(),
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("\nTask updated successfully.")
        } catch (e: Exception) {
            printer.displayLn("\nFailed to update task: ${e.message}")
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

    private fun showStates(states: List<State>) {
        printer.displayLn("\nAvailable States:")
        states.forEachIndexed { index, state ->
            printer.displayLn("${index + 1}. ${state.name}")
        }
    }

    private fun promptSelection(message: String, max: Int): Int {
        while (true) {
            printer.display(message)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.displayLn("\nPlease enter a valid number between 1 and $max.")
        }
    }

    private fun promptString(message: String, currentValue: String): String {
        printer.display(message)
        val input = inputReader.readString()
        return if (input.isNullOrBlank()) currentValue else input
    }

    private fun promptDate(message: String, currentValue: LocalDate): LocalDate {
        printer.display(message)
        val input = inputReader.readString()
        return if (input.isNullOrBlank()) {
            currentValue
        } else {
            try {
                dateParser.parseDateFromString(input)
            } catch (e: Exception) {
                printer.displayLn("\nInvalid date format. Keeping current value.")
                currentValue
            }
        }
    }
}
