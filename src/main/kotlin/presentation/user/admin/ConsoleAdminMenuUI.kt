package presentation.user.admin

import org.koin.core.annotation.Named
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.system.exitProcess

class ConsoleAdminMenuUI(
    @Named("mate") private val uiFeatures: List<UIFeature>,
    private val printer: Printer,
    private val promptUtils: PromptUtils,
) : UiLauncher {

    override suspend fun launchUi() {
        sortMenu()
        showWelcome()
        presentFeature()
    }

    private fun showWelcome() {
        printer.displayLn("Welcome to PlanMate Admin Dashboard ")
    }

    private suspend fun presentFeature() {
        showOptions()
        when (val input = promptUtils.promptNonEmptyInt("\nEnter your choice: ")) {
            in 1..uiFeatures.size -> {
                uiFeatures.find { it.id == input }?.uiLauncher?.launchUi()
            }
            0 -> {
                exitProcess(0)
            }
            else -> {
                printer.displayLn("Invalid input")
            }
        }
        presentFeature()
    }

    private fun showOptions() {
        printer.displayLn("╔══════════════════════════════════════╗")
        printer.displayLn("║        PlanMate Admin Dashboard      ║")
        printer.displayLn("╠══════════════════════════════════════╣")
        uiFeatures.sortedBy { it.id }.forEach { printFeatureLine(it) }
        printer.displayLn("║  0.  Exit                            ║")
        printer.displayLn("╚══════════════════════════════════════╝")
    }

    private fun printFeatureLine(featureUi: UIFeature) {
        printer.displayLn("║ ${(featureUi.id).toString().padStart(2, ' ')}.  ${featureUi.label.padEnd(32)}║")
    }

    private fun sortMenu() {
        uiFeatures.sortedBy { it.id }
    }

}