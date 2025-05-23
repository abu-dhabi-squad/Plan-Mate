package presentation.user.mate

import org.koin.core.annotation.Named
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.system.exitProcess

class ConsoleUserMenuUI(
    @Named("mate") private val uiFeatures: List<UIFeature>,
    private val printer: Printer,
    private val promptUtils: PromptUtils
    ) : UiLauncher {
    override suspend fun launchUi() {
        sortMenu()
        showWelcome()
        presentFeature()
    }

    private fun showWelcome() {
        printer.displayLn("Welcome to PlanMate App ")
    }

    private suspend fun presentFeature() {
        showOptions()
        when (val input =promptUtils.promptNonEmptyInt("Enter your choice: ")) {
            in 1..uiFeatures.size -> {
                uiFeatures.find { it.id == input }?.uiLauncher?.launchUi()
            }
            0 -> {
                exitProcess(0)
            }
            else -> {
                printer.displayLn("\nInvalid input")
            }
        }
        presentFeature()
    }

    private fun showOptions() {
        printer.displayLn("╔══════════════════════════════════════╗")
        printer.displayLn("║        PlanMate User Dashboard       ║")
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