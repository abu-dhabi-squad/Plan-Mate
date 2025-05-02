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
        printer.displayLn()
        printer.displayLn("╔════════════════════════════════════════╗")
        printer.displayLn("║         PlanMate Admin Console         ║")
        printer.displayLn("╠════════════════════════════════════════╣")
        printer.displayLn("╠ 1. Add a new Project                   ╣")
        printer.displayLn("╠ 2. Edit an Existing Project            ╣")
        printer.displayLn("╠ 3. Delete a Project                    ╣")
        printer.displayLn("╠ 4. Add a new Task                      ╣")
        printer.displayLn("╠ 5. View All Tasks in a Project         ╣")
        printer.displayLn("╠ 6. Create a new User (Mate)            ╣")
        printer.displayLn("╠ 7. View History of a Project           ╣")
        printer.displayLn("╠ 8. View History of a Task              ╣")
        printer.displayLn("╠ 9. Edit Task/Project States            ╣")
        printer.displayLn("╠ 10. Log Out                            ╣")
        printer.displayLn("╚════════════════════════════════════════╝")
    }

}