package presentation.taskmanagement

import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.displaySwimlanesByState
import presentation.utils.extensions.printWithStates

class GetTasksByProjectIdUI(
    private val printer: Printer,
    private val promptUtils: PromptUtils,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to load projects: ${exception.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }
        printer.displayLn("\n=== Available Projects ===")
        projects.printWithStates(printer)
        val projectIndex = promptUtils.promptSelectionIndex("\nEnter project number", projects.size)
        val selectedProject = projects[projectIndex]

        val tasks = try {
            getTasksByProjectIdUseCase(selectedProject.projectId)
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to load tasks: ${exception.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks found in '${selectedProject.projectName}'.")
            return
        }
        tasks.displaySwimlanesByState(selectedProject.projectName, selectedProject.taskStates, printer)
    }
}

