package squad.abudhabi.presentation.project

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
        TODO("Not yet implemented")
    }
}