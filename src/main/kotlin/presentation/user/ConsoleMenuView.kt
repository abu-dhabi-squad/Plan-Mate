package squad.abudhabi.presentation.user

import squad.abudhabi.logic.model.User
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import kotlin.system.exitProcess

class ConsoleMenuView(
    private val user: User,
    // other views will be injected here
    private val printer: Printer,
    private val inputReader: InputReader
) : UiLauncher {
    override fun launchUi() {
        showMateMenu()
        printer.display("Enter your choice: ")
        val input = inputReader.readInt()
        when (input) {
            1 -> {/* here will lan*/
            }

            6 -> exitProcess(0)
        }
        launchUi()
    }

    private fun showMateMenu() {
        printer.display("╔════════════════════════════════════════╗")
        printer.display("║         PlanMate Mate Console          ║")
        printer.display("╠════════════════════════════════════════╣")
        printer.display("╠ 1. Add a new Task                      ╣")
        printer.display("╠ 2. Edit a Task                         ╣")
        printer.display("╠ 3. Delete a Task                       ╣")
        printer.display("╠ 4. View All Tasks in a Project         ╣")
        printer.display("╠ 5. View History of a Task              ╣")
        printer.display("╠ 6. Log Out                             ╣")
        printer.display("╚════════════════════════════════════════╝")
    }
}