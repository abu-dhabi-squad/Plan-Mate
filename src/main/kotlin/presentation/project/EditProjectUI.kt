package squad.abudhabi.presentation.project

import squad.abudhabi.logic.project.EditProjectUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class EditProjectUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val reader: InputReader,
    private val printer: Printer
) : UiLauncher {
    override fun launchUi() {
        printer.display("enter project id: ")
        reader.readString()?.let { projectId ->
            try {
                printer.display("\nenter the new name: ")
                reader.readString()?.let { projectName ->
                    editProjectUseCase(projectId, projectName)
                } ?: printer.displayLn("wrong input")
            } catch (e: Exception) {
                printer.displayLn(e.message)
            }
        } ?: printer.displayLn("wrong input")
    }
}