package squad.abudhabi.presentation.admin

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
        showMenu()
        printer.display("Enter your choice: ")
        val input = inputReader.readInt()
        when (input) {
            1 -> {/* here will lan*/ }

            10 -> exitProcess(0)
        }
        launchUi()
    }

    private fun showMenu() {
        printer.display("╔════════════════════════════════════════╗")
        printer.display("║         PlanMate Admin Console        ║")
        printer.display("╠════════════════════════════════════════╣")
        printer.display("╠ 1. Add a new Project                  ╣")
        printer.display("╠ 2. Edit an Existing Project           ╣")
        printer.display("╠ 3. Delete a Project                   ╣")
        printer.display("╠ 4. Add a new Task                     ╣")
        printer.display("╠ 5. View All Tasks in a Project        ╣")
        printer.display("╠ 6. Create a new User (Mate)           ╣")
        printer.display("╠ 7. View History of a Project          ╣")
        printer.display("╠ 8. View History of a Task             ╣")
        printer.display("╠ 9. Edit Task/Project States           ╣")
        printer.display("╠ 10. Log Out                           ╣")
        printer.display("╚════════════════════════════════════════╝")
    }

}