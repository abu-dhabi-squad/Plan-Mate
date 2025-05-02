package squad.abudhabi.presentation.taskManagment

import squad.abudhabi.logic.task.GetTaskByIdUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class GetTaskByIdPresenterUI(
    private val printer: Printer,
    private val inputReader: InputReader,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : UiLauncher {

    override fun launchUi() {
        printer.display("🔍 Retrieve Task by ID")
        printer.display("Enter task ID:")

        val taskId = inputReader.readString()
        if (taskId.isNullOrBlank()) {
            printer.display("❌ Task ID cannot be empty.")
            return
        }

        try {
            val task = getTaskByIdUseCase(taskId)
            printer.display("✅ Task Found:")
            printer.display("Title: ${task.title}")
            printer.display("Description: ${task.description}")
            printer.display("Start Date: ${task.startDate}")
            printer.display("End Date: ${task.endDate}")
            printer.display("Project ID: ${task.projectId}")
            printer.display("State ID: ${task.stateId}")
            printer.display("Assigned to: ${task.userName}")
        } catch (e: Exception) {
            printer.display("❌ Failed to retrieve task: ${e.message}")
        }
    }
}
