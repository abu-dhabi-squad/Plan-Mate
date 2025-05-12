package presentation.presentation.utils.extensions

import logic.model.Task
import presentation.io.Printer

fun List<Task>.showAll(printer: Printer) {
    if (isEmpty()) {
        printer.displayLn("\nNo tasks available in this project.")
        return
    }
    printer.displayLn("\nTasks in Project:")
    this.forEachIndexed { idx, task ->
        task.displayAt(idx + 1, printer)
    }
    printer.displayLn()
}

fun Task.displayAt(index: Int, printer: Printer) {
    printer.displayLn(
        """
        ${index}. $title
           ↳ Description: $description
           ↳ Start: $startDate, End: $endDate
           ↳ Assigned to: $username
           ↳ TaskState ID: $taskStateId
        """.trimIndent()
    )
}
