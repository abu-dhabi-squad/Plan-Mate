package presentation.taskmanagement

import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.printWithStates
import presentation.presentation.utils.extensions.showAll

class GetTasksByProjectIdUI(
    private val printer: Printer,
    private val promptService: PromptService,
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
        printer.displayLn("\n=== Available Projects ===")
        projects.printWithStates(printer)
        val projectIndex =
            promptService.promptSelectionIndex("\nEnter project number: ", projects.size)
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
        tasks.showAll(printer)
    }

}

