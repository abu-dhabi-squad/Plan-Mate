package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.project.GetAllProjectsUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

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
            printer.display("Failed to fetch projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.display("No projects available.")
            return
        }

        printer.displayLn("=== Available Projects ===")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }

        printer.display("Enter project number: ")
        val choice = reader.readInt()
        if (choice == null) {
            printer.display("Input cannot be empty.")
            return
        }

        if (choice !in 1..projects.size) {
            printer.display("Input cannot be out projects range.")
            return
        }
        val selected = projects.getOrNull(choice - 1)
        if (selected == null) {
            printer.display("Invalid project selection.")
            return
        }

        showAuditLogs(selected.id.toString())
    }

    private suspend fun showAuditLogs(entityId: String) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            if (audits.isEmpty()) {
                printer.display("No audit logs found for this project.")
                return
            }

            printer.display("=== Audit Logs for Project ===")
            audits.forEachIndexed { index, audit ->

                printer.displayLn("${index + 1}. Entity: ${audit.entityId}, Created By: ${audit.createdBy}, Date: ${audit.date}")
                if (audit.oldState.isEmpty()) printer.display("New state set as ${audit.newState}")
                else printer.display("Changed from ${audit.oldState} to ${audit.newState}")
            }

        } catch (e: Exception) {
            printer.display("error: ${e.message}")
        }
    }
}
