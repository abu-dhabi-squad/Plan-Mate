package squad.abudhabi.presentation.project

import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class GetAllProjectsUI (
    private val inputReader: InputReader,
    private val printer: Printer,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
): UiLauncher{
    override fun launchUi() {
      printer.displayLn("all projects created : \n")
        getAllProjects()
    }
    private fun getAllProjects(){
        try {
          getAllProjectsUseCase().forEachIndexed {index,project->
              printer.displayLn("${index+1}) ${project.projectName}")
          }
        }catch (e: Exception){
            printer.displayLn(e.message)
        }
    }
}