package presentation.task_management

import logic.audit.CreateAuditUseCase
import logic.validation.DateParser
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.CreateTaskUseCase
import squad.abudhabi.logic.user.GetLoggedUserUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import java.time.LocalDate

class CreateTaskPresenterUI(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val parserDate: DateParser,
    private val createAuditUseCase: CreateAuditUseCase

) : UiLauncher {

    override fun launchUi() {
        val title = promptNonEmptyString("Enter task title:")
        val description = promptNonEmptyString("Enter task description:")
        val startDate = promptDate("Enter task start date (YYYY-MM-DD):")
        val endDate = promptDate("Enter task end date (YYYY-MM-DD):")

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
        val projectIndex = promptSelectionIndex("Enter project number:", projects.size)
        val selectedProject = projects[projectIndex]

        showStates(selectedProject.states)
        val stateIndex = promptSelectionIndex("Enter state number:", selectedProject.states.size)
        val selectedState = selectedProject.states[stateIndex]

        val task = Task(
            userName = getLoggedUserUseCase().username,
            projectId = selectedProject.id,
            stateId = selectedState.id,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate
        )

        try {
            createTaskUseCase(task)
            createAuditUseCase(
                Audit(
                    entityId = task.id,
                    entityType = EntityType.TASK,
                    oldState = "",
                    newState = "Created",
                    createdBy = "Noor Serry"
                )
            )
            printer.display("Task created successfully.")
        } catch (e: Exception) {
            printer.display("Failed to create task: ${e.message}")
        }
    }

    private fun showProjects(projects: List<Project>) {
        printer.display("Available projects:")
        projects.forEachIndexed { index, project ->
            printer.display("${index + 1}. ${project.projectName}")
        }
    }

    private fun showStates(states: List<State>) {
        printer.display("Available states:")
        states.forEachIndexed { index, state ->
            printer.display("${index + 1}. ${state.name}")
        }
    }

    private fun promptNonEmptyString(prompt: String): String {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readString()
            if (!input.isNullOrBlank()) return input
            printer.display("Input cannot be empty.")
        }
    }

    private fun promptDate(prompt: String): LocalDate {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readString()
            if (input.isNullOrBlank()) {
                printer.display("Date cannot be empty.")
                continue
            }
            try {
                val date = parserDate.parseDateFromString(input)
                return date
            } catch (e: Exception) {
                printer.display("Invalid date format. Please use YYYY-MM-DD.")
            }
        }
    }

    private fun promptSelectionIndex(prompt: String, size: Int): Int {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readInt()
            if (input != null && input in 1..size) return input - 1
            printer.display("Please enter a number between 1 and $size.")
        }
    }
}
