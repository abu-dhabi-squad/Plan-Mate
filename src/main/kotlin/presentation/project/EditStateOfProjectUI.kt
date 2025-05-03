package squad.abudhabi.presentation.project

import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.Printer

class EditStateOfProjectUI(
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val reader: ConsoleReader,
    private val printer: Printer
) : UiLauncher {
    override fun launchUi() {
        try {
            getAllProjectsUseCase().takeIf { it.isNotEmpty() }
                ?.forEach { project ->
                    printer.displayLn("project id: " + project.id + " - project name: " + project.projectName + " - states : " + project.states)
                }?.let {
                    printer.display("enter project id: ")
                    reader.readString()?.let { projectId ->
                        printer.display("\nenter the id of state you want to edit: ")
                        reader.readString()?.let { stateId ->
                            printer.display("\nenter the new name of the state: ")
                            reader.readString()?.let { stateNewName ->
                                editStateOfProjectUseCase(projectId, State(stateId, stateNewName))
                            } ?: printer.displayLn("wrong input")
                        } ?: printer.displayLn("wrong input")
                    } ?: printer.displayLn("wrong input")
                } ?: printer.displayLn("there is no project in list")
        } catch (e: Exception) {
            printer.displayLn(e.message)
        }
    }
}