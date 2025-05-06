package presentation.task_management

import logic.task.GetTaskByIdUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class GetTaskByIdPresenterUI(
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : UiLauncher {

    override suspend fun launchUi() {
        printer.displayLn("Retrieve Task by ID")
        printer.display("Enter task ID:")

        val taskId = inputReader.readString()
        if (taskId.isNullOrBlank()) {
            printer.displayLn("Task ID cannot be empty.")
            return
        }

        try {
            val task = getTaskByIdUseCase(taskId)
            printer.displayLn("Task Found:")
            printer.displayLn("Title: ${task.title}")
            printer.displayLn("Description: ${task.description}")
            printer.displayLn("Start Date: ${task.startDate}")
            printer.displayLn("End Date: ${task.endDate}")
            printer.displayLn("Project ID: ${task.projectId}")
            printer.displayLn("State ID: ${task.stateId}")
            printer.displayLn("Assigned to: ${task.userName}")
        } catch (e: Exception) {
            printer.displayLn("Failed to retrieve task: ${e.message}")
        }
    }
}
