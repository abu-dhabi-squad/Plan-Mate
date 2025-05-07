package presentation.task_management

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
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import logic.user.GetLoggedUserUseCase
import java.time.LocalDate

class EditTaskPresenterUI(
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
            printer.displayLn("Failed to load projects: ${e.message}")
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
            getTasksByProjectIdUseCase(selectedProject.id.toString())
        } catch (e: Exception) {
            printer.displayLn("Failed to load tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("No tasks found in this project.")
            return
        }

        showTasks(tasks)
        val taskIndex = promptSelection("Select a task to edit:", tasks.size)
        val selectedTask = tasks[taskIndex]

        printer.displayLn("Editing Task: ${selectedTask.title}")
        val newTitle =
            promptString("Enter new title (leave blank to keep current):", selectedTask.title)
        val newDescription = promptString(
            "Enter new description (leave blank to keep current):",
            selectedTask.description
        )
        val newStartDate =
            promptDate("Enter new start date (YYYY-MM-DD) or leave blank:", selectedTask.startDate)
        val newEndDate =
            promptDate("Enter new end date (YYYY-MM-DD) or leave blank:", selectedTask.endDate)

        showStates(selectedProject.states)
        val stateIndex = promptSelection("Select new state:", selectedProject.states.size)
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
                    oldState = oldState.id,
                    newState = newState.id,
                    createdBy = getLoggedUserUseCase().username
                )
            )
            printer.displayLn("✅ Task updated successfully.")
        } catch (e: Exception) {
            printer.displayLn("❌ Failed to update task: ${e.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.displayLn("📁 Available Projects:")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }
    }

    private fun showTasks(tasks: List<Task>) {
        printer.displayLn("📋 Tasks in Selected Project:")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title} (ID: ${task.id})")
        }
    }

    private fun showStates(states: List<State>) {
        printer.displayLn("📍 Available States:")
        states.forEachIndexed { index, state ->
            printer.displayLn("${index + 1}. ${state.name}")
        }
    }

    private fun promptSelection(message: String, max: Int): Int {
        while (true) {
            printer.display(message)
            val input = inputReader.readInt()
            if (input != null && input in 1..max) return input - 1
            printer.displayLn("Please enter a valid number between 1 and $max.")
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
                printer.displayLn("⚠️ Invalid date format. Keeping current value.")
                currentValue
            }
        }
    }
}
