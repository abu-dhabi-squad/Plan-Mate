package squad.abudhabi.presentation.project

import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.Printer

class EditStateOfProjectUI(
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase,
    private val reader: ConsoleReader,
    private val printer: Printer
) : UiLauncher {
    override fun launchUi() {
        printer.display("enter project id: ")
        reader.readString()?.let { projectId ->
            try {
                printer.display("\nenter the id of state you want to edit: ")
                reader.readString()?.let { stateId ->
                    printer.display("\nenter the new name of the state: ")
                    reader.readString()?.let { stateNewName ->
                        editStateOfProjectUseCase(projectId, State(stateId, stateNewName))
                    } ?: printer.displayLn("wrong input")
                } ?: printer.displayLn("wrong input")
            } catch (e: Exception) {
                printer.displayLn(e.message)
            }
        } ?: printer.displayLn("wrong input")
    }
}