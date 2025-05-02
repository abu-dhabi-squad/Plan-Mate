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
        TODO("Not yet implemented")
    }
}