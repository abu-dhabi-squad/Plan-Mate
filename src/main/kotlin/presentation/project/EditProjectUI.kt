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
                .let { projects ->
                    projects?.forEachIndexed { index, project ->
                        printer.displayLn("${index + 1}- Project Name: " + project.projectName + " - States : " + project.states)
                    }?.let {
                        printer.display("\nChoose project: ")
                        reader.readInt()?.let { choice ->
                            val projectIndex = choice - 1
                            if (projectIndex !in projects.indices) {
                                printer.displayLn("Wrong input")
                                return
                            }
                            printer.displayLn("\nEnter the new name: ")
                            reader.readString()?.let { projectName ->
                                editProjectUseCase(projects[projectIndex].id.toString(), projectName)
                                printer.displayLn("\nProject updated successfully.")
                            } ?: printer.displayLn("Wrong input")
                        } ?: printer.displayLn("Wrong input")
                    } ?: printer.displayLn("There is no project in list")
                }
        } catch (e: Exception) {
            printer.displayLn(e.message)
        }
    }
}