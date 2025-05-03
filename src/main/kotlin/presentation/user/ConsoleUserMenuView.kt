package presentation.user

import org.koin.core.annotation.Named
import org.koin.ext.getFullName
import squad.abudhabi.logic.model.User
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import kotlin.system.exitProcess

class ConsoleUserMenuView(
    private val user: User,
    @Named("mate") private val launchers: List<UiLauncher>,
    private val printer: Printer,
    private val inputReader: InputReader
) : UiLauncher {
    override fun launchUi() {
        showMateMenu()
        printer.display("Enter your choice: ")
        val input = inputReader.readInt()
          if(input in 0 .. launchers.size)
              launchers[input!!].launchUi()
        else if(input == launchers.size)
            exitProcess(0)
        else
          printer.displayLn("Invailed Inpur")
        launchUi()
    }

    private fun showMateMenu() {
//        printer.displayLn()
//        printer.displayLn("╔════════════════════════════════════════╗")
//        printer.displayLn("║         PlanMate Mate Console          ║")
//        printer.displayLn("╠════════════════════════════════════════╣")
//        printer.displayLn("╠ 1. Add a new Task                      ╣")
//        printer.displayLn("╠ 2. Edit a Task                         ╣")
//        printer.displayLn("╠ 3. Delete a Task                       ╣")
//        printer.displayLn("╠ 4. View All Tasks in a Project         ╣")
//        printer.displayLn("╠ 5. View History of a Task              ╣")
//        printer.displayLn("╠ 6. Log Out                             ╣")
//        printer.displayLn("╚════════════════════════════════════════╝")
        var index = 0
        for(i  in launchers)
            printer.displayLn(  "${index++} - "+ i::class.getFullName())
    }
}