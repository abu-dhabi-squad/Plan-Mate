package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.showAuditLogs
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetAuditForProjectUI(
    private val promptUtils: PromptUtils,
    private val printer: Printer,
    private val getAuditUseCase: GetAuditUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : UiLauncher {
    override suspend fun launchUi() {
        val projects = try {
            getAllProjectsUseCase()
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to fetch projects: ${exception.message}")
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
        val projectIndex = promptUtils.promptSelectionIndex("\nEnter project number", projects.size)
        val selectedProject = projects[projectIndex]
        showAuditLogs(selectedProject.projectId)
    }

    private suspend fun showAuditLogs(entityId: Uuid) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            audits.showAuditLogs(printer)
        } catch (exception: Exception) {
            printer.displayLn("\nError: ${exception.message}")
        }
    }
}
