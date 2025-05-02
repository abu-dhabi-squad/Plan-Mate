package squad.abudhabi.presentation.project

import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.CreateProjectUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import java.util.*

class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val inputReader: InputReader,
    private val printer: Printer
):UiLauncher {

    override fun launchUi() {
        printer.display("Enter project name: ")
        val projectName = inputReader.readString()?.takeIf { it.isNotBlank() }
        if (projectName == null) {
            printer.displayLn("Project name cannot be empty.")
            return
        }

        printer.display("Enter number of states: ")
        val stateCount = inputReader.readInt()
        if (stateCount == null || stateCount < 0) {
            printer.displayLn("Invalid number of states.")
            return
        }

        val states = mutableListOf<State>()
        for (i in 1..stateCount) {
            printer.display("Enter name for state #$i: ")
            val stateName = inputReader.readString()?.takeIf { it.isNotBlank() }
            if (stateName == null) {
                printer.displayLn("State name cannot be empty.")
                return
            }
            states.add(State(name = stateName))
        }

        createProjectUseCase(projectName, states)
        printer.displayLn("Project '$projectName' created with ${states.size} state(s).")
    }
}