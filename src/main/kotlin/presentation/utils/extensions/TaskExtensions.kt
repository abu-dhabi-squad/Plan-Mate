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

private fun getLongestCellWidth(grouped: Map<UUID, List<Task>>) =
    grouped.values.maxOfOrNull { it.maxOfOrNull { task -> task.title.length + task.username.length + 3 } ?: 0 } ?: 0

private fun displayHeader(
    states: List<TaskState?>,
    printer: Printer,
    padValue: Int
) {
    states.forEach { printer.display("| ${it?.stateName?.padEnd(padValue)} ") }
    printer.displayLn("|")
}

private fun displayRows(
    maxRows: Int,
    states: List<TaskState?>,
    grouped: Map<UUID, List<Task>>,
    padValue: Int,
    printer: Printer
) {
    for (i in 0 until maxRows) {
        states.forEach { state ->
            val task = grouped[state?.stateId ?: UUID.randomUUID()]?.getOrNull(i)
            val display = if (task != null) {
                "${task.title} (${task.username})".padEnd(padValue)
            } else {
                "".padEnd(padValue)
            }
            printer.display("| ${display.padEnd(20)} ")
        }
        printer.displayLn("|")
    }
    printer.displayLn()
}