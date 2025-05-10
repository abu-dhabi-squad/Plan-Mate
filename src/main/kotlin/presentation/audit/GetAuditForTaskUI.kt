package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.model.Project
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class GetAuditForTaskUI(
    private val printer: Printer,
    private val reader: InputReader,
    private val getAuditUseCase: GetAuditUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
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

        val projectIndex = promptNonEmptyInt("\nEnter project number: ") - 1
        if (projectIndex !in projects.indices) {
            printer.displayLn("\nInput cannot be out projects range.")
            return
        }

        showTaskAudit(projects[projectIndex])
    }

    private suspend fun showTaskAudit(project: Project) {
        val tasks = try {
            getTasksByProjectIdUseCase(project.id)
        } catch (e: Exception) {
            printer.displayLn("\nFailed to fetch tasks: ${e.message}")
            return
        }
        if (tasks.isEmpty()) {
            printer.displayLn("\nNo tasks available in this project.")
            return
        }
        printer.displayLn("\n=== Tasks in Project: ${project.projectName} ===")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title}")
        }
        val taskIndex = promptNonEmptyInt("\nEnter task number: ") - 1
        if (taskIndex !in tasks.indices) {
            printer.displayLn("\nInput cannot be out tasks range.")
            return
        }
        showAuditLogs(tasks[taskIndex].id.toString())
    }

    private suspend fun showAuditLogs(entityId: String) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            if (audits.isEmpty()) {
                printer.displayLn("\nNo audit logs found for this task.")
                return
            }

            printer.displayLn("\n=== Audit Logs for Task ===")
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

    private fun promptNonEmptyInt(prompt: String): Int {
        while (true) {
            printer.display(prompt)
            val input = reader.readInt()
            if (input != null) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }
}
