package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class GetAuditForProjectUI(
    private val printer: Printer,
    private val reader: InputReader,
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

        printer.display("\nEnter project number: ")
        val choice = reader.readInt()
        if (choice == null) {
            printer.displayLn("\nInput cannot be empty.")
            return
        }

        if (choice !in 1..projects.size) {
            printer.displayLn("\nInput cannot be out projects range.")
            return
        }
        val selected = projects.getOrNull(choice - 1)
        if (selected == null) {
            printer.displayLn("\nInvalid project selection.")
            return
        }

        showAuditLogs(selected.id.toString())
    }

    private suspend fun showAuditLogs(entityId: String) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            if (audits.isEmpty()) {
                printer.displayLn("\nNo audit logs found for this project.")
                return
            }

            printer.displayLn("\n=== Audit Logs for Project ===")
            audits.forEachIndexed { index, audit ->
                printer.displayLn("${index + 1}. Date: ${audit.date}, Created By: ${audit.createdBy}")
                if (audit.oldState.isEmpty()) printer.displayLn("\t=> New state set as ${audit.newState}")
                else printer.displayLn("\t=> Changed from ${audit.oldState} to ${audit.newState}")
            }
            printer.displayLn()
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }
}
