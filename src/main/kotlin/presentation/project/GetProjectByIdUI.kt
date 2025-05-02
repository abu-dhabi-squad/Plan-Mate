package squad.abudhabi.presentation.project

import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class GetProjectByIdUI (
    private val inputReader: InputReader,
    private val printer: Printer,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : UiLauncher {
    override fun launchUi() {
        printer.displayLn("enter project id")
        val id=inputReader.readString()
        if (id!=null){
            getProjectById(id)
        }
    }

    private fun getProjectById(projectId: String) {
        try {
            val project = getProjectByIdUseCase(projectId)
            printer.displayLn(project.projectName)
        } catch (e: Exception) {
            printer.displayLn(e.message)
        }
    }
}
