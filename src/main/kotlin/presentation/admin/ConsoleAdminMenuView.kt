package presentation.admin

import org.koin.core.annotation.Named
import org.koin.ext.getFullName
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import squad.abudhabi.logic.model.User
import kotlin.system.exitProcess

class ConsoleAdminMenuView(
    private val user: User,
    @Named("admin") private val launchers: List<UiLauncher>,
    private val printer: Printer,
    private val inputReader: InputReader,
) : UiLauncher {

    override fun launchUi() {

        showMenu()
        printer.display("Enter your choice: ")
        val input = inputReader.readInt()
        if (input in 0..launchers.size)
            launchers[input!!].launchUi()
        else if (input == launchers.size)
            exitProcess(0)
        else
            printer.displayLn("Invailed Inpur")
        launchUi()
    }

    private fun showMenu() {
//        printer.displayLn()
//        printer.displayLn("╔════════════════════════════════════════╗")
//        printer.displayLn("║         PlanMate Admin Console         ║")
//        printer.displayLn("╠════════════════════════════════════════╣")
//        printer.displayLn("╠ 1. Add a new Project                   ╣")
//        printer.displayLn("╠ 2. Edit an Existing Project            ╣")
//        printer.displayLn("╠ 3. Delete a Project                    ╣")
//        printer.displayLn("╠ 4. Add a new Task                      ╣")
//        printer.displayLn("╠ 5. View All Tasks in a Project         ╣")
//        printer.displayLn("╠ 6. Create a new User (Mate)            ╣")
//        printer.displayLn("╠ 7. View History of a Project           ╣")
//        printer.displayLn("╠ 8. View History of a Task              ╣")
//        printer.displayLn("╠ 9. Edit Project States                 ╣")
//        printer.displayLn("╠ 10. Edit Project States                ╣")
//        printer.displayLn("╠ 10. Log Out                            ╣")
//        printer.displayLn("╚════════════════════════════════════════╝")
        var index = 0
        for (i in launchers)
            printer.displayLn("${index++} - " + i::class.getFullName())
    }

}