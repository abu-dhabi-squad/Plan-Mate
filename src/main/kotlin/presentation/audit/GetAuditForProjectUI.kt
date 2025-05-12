package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.showAuditLogs
import java.util.UUID

class GetAuditForProjectUI(
    private val promptService: PromptService,
    private val printer: Printer,
    private val getAuditUseCase: GetAuditUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.displayLn("\nFailed to fetch projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("\nNo projects available.")
            return
        }

        printer.displayLn("\n=== Available Projects ===")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }

        val choice = promptService.promptSelectionIndex("\nEnter project number: ",projects.size)

        val selected = projects.get(choice)

        showAuditLogs(selected.projectId)
    }

    private suspend fun showAuditLogs(entityId: UUID) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            audits.showAuditLogs(printer)
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }
}
