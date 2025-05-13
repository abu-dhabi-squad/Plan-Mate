package presentation.utils.extensions

import logic.model.Project
import presentation.io.Printer

fun List<Project>.printWithStates(printer: Printer) {
    if (isEmpty()) {
        printer.displayLn("\nThere are no projects in the list.")
        return
    }
    this.forEachIndexed { index, project ->
        printer.display("${index + 1}- Project Name: ${project.projectName} - States: [ ")
        project.taskStates.forEachIndexed { stateIndex, state ->
            printer.display("${stateIndex + 1}- TaskState Name: ${state.stateName}")
            if (stateIndex != project.taskStates.size - 1) printer.display(", ")
        }
        printer.displayLn(" ]")
    }
}