package presentation.project

import logic.project.EditProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class EditProjectUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val reader: InputReader,
    private val printer: Printer
) : UiLauncher {
    override suspend fun launchUi() {
        try {
            getAllProjectsUseCase().takeIf { it.isNotEmpty() }
                ?.forEach { project ->
                    printer.displayLn("project id: " + project.id + " - project name: " + project.projectName + " - states : " + project.states)
                }?.let {
                    printer.display("enter the id of the project you want to edit: ")
                    reader.readString()?.let { projectId ->
                        printer.display("\nenter the new name: ")
                        reader.readString()?.let { projectName ->
                            editProjectUseCase(projectId, projectName)
                        } ?: printer.displayLn("wrong input")
                    } ?: printer.displayLn("wrong input")
                } ?: printer.displayLn("there is no project in list")
        } catch (e: Exception) {
            printer.displayLn(e.message)
        }

    }
}