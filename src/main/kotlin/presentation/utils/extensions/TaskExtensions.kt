package presentation.utils.extensions

import logic.model.Task
import logic.model.TaskState
import presentation.io.Printer
import java.util.UUID

fun List<Task>.displaySwimlanesByState(projectName: String, taskStates: List<TaskState>, printer: Printer) {
    val grouped = groupTasksByState(this)
    val maxRows = getMaxRows(grouped)
    val paddingValue = getMaxPaddingValue(taskStates, grouped)
    printer.displayLn("\n===== Project: $projectName =====\n")
    displayHeader(taskStates, printer, paddingValue)
    displayRows(maxRows, taskStates, grouped, paddingValue, printer)
}

private fun groupTasksByState(tasks: List<Task>): Map<UUID, List<Task>> {
    return tasks.groupBy { it.taskStateId }
}

private fun getMaxRows(grouped: Map<UUID, List<Task>>) =
    grouped.values.maxOfOrNull { it.size } ?: 0

private fun getMaxPaddingValue(
    taskStates: List<TaskState>,
    grouped: Map<UUID, List<Task>>
): Int {
    val longestLabelWidth = getLongestLabelWidth(taskStates)
    val longestCellWidth = getLongestCellWidth(grouped)
    val padValue = maxOf(longestLabelWidth, longestCellWidth, 20)
    return padValue
}

private fun getLongestLabelWidth(states: List<TaskState?>) =
    states.maxOfOrNull { it?.stateName?.length ?: 0 } ?: 0

private fun getLongestCellWidth(grouped: Map<UUID, List<Task>>): Int {
    val r1 = grouped.values.maxOfOrNull { it.maxOfOrNull { task -> task.title.length + task.username.length + 3 } ?: 0 } ?: 0
    val r2 = grouped.values.maxOfOrNull { it.maxOfOrNull { task -> task.startDate.toString().length + task.endDate.toString().length + 3 } ?: 0 } ?: 0
    return maxOf(r1, r2)
}

private fun displayHeader(
    states: List<TaskState?>,
    printer: Printer,
    padValue: Int
) {
    displayHorizontalBorder(printer, padValue, states)
    states.forEach { printer.display("| ${it?.stateName?.padEnd(padValue)} ") }
    printer.displayLn("|")
    displayHorizontalBorder(printer, padValue, states)
}

private fun displayRows(
    maxRows: Int,
    states: List<TaskState?>,
    grouped: Map<UUID, List<Task>>,
    padValue: Int,
    printer: Printer
) {
    for (i in 0 until maxRows) {
        displayRow(states, grouped, i, padValue, printer) { task -> "${task.title} (${task.username})" }
        displayRow(states, grouped, i, padValue, printer) { task -> "${task.startDate} - ${task.endDate}" }
        displayHorizontalBorder(printer, padValue, states)
    }
    printer.displayLn()
}

private fun displayRow(
    states: List<TaskState?>,
    grouped: Map<UUID, List<Task>>,
    i: Int,
    padValue: Int,
    printer: Printer,
    getCellValue: (Task) -> String
) {
    states.forEach { state ->
        val task = grouped[state?.stateId ?: UUID.randomUUID()]?.getOrNull(i)
        val display = if (task != null) {
            getCellValue(task).padEnd(padValue)
        } else {
            "".padEnd(padValue)
        }
        printer.display("| ${display.padEnd(padValue)} ")
    }
    printer.displayLn("|")
}

private fun displayHorizontalBorder(
    printer: Printer,
    padValue: Int,
    states: List<TaskState?>
) = printer.displayLn("-".repeat(padValue * states.size + states.size * 3 + 1))