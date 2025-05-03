package presentation.audit

import logic.audit.GetAuditUseCase
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.Project
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class GetAuditForTaskUI(
    private val printer: Printer,
    private val reader: InputReader,
    private val getAuditUseCase: GetAuditUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : UiLauncher {

    override fun launchUi() {

        val projects = try {
            getAllProjectsUseCase()
        } catch (e: Exception) {
            printer.displayLn("Failed to fetch projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.displayLn("No projects available.")
            return
        }

        printer.displayLn("=== Available Projects ===")
        projects.forEachIndexed { index, project ->
            printer.displayLn("${index + 1}. ${project.projectName}")
        }

        printer.display("Enter project number: ")
        val projectChoice = reader.readInt()
        if (projectChoice == null) {
            printer.displayLn("Input cannot be empty.")
            return
        }
        if (projectChoice !in 1..projects.size) {
            printer.displayLn("Input cannot be out projects range.")
            return
        }
        val selectedProject = projects.getOrNull(projectChoice - 1)
        if (selectedProject == null) {
            printer.displayLn("Invalid project selection.")
            return
        }

        showTaskAudit(selectedProject)
    }

    private fun showTaskAudit(project: Project) {

        val tasks = try {
            getTasksByProjectIdUseCase(project.id)
        } catch (e: Exception) {
            printer.displayLn("Failed to fetch tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.displayLn("No tasks available in this project.")
            return
        }

        printer.displayLn("=== Tasks in Project: ${project.projectName} ===")
        tasks.forEachIndexed { index, task ->
            printer.displayLn("${index + 1}. ${task.title}")
        }

        printer.display("Enter task number: ")
        val taskChoice = reader.readInt()
        if (taskChoice == null) {
            printer.displayLn("Input cannot be empty.")
            return
        }
        if (taskChoice !in 1..tasks.size) {
            printer.displayLn("Input cannot be out tasks range.")
            return
        }
        val selectedTask = tasks.getOrNull(taskChoice - 1)
        if (selectedTask == null) {
            printer.displayLn("Invalid task selection.")
            return
        }

        showAuditLogs(selectedTask.id)
    }

    private fun showAuditLogs(entityId: String) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            if (audits.isEmpty()) {
                printer.displayLn("No audit logs found for this task.")
                return
            }

            printer.displayLn("=== Audit Logs for Task ===")
            audits.forEachIndexed { index, audit ->
                printer.displayLn("${index + 1}. Entity: ${audit.entityId}, Created By: ${audit.createdBy}, Date: ${audit.date}")
                if (audit.oldState.isEmpty()) printer.displayLn("New state set as ${audit.newState}")
                else printer.displayLn("Changed from ${audit.oldState} to ${audit.newState}")
            }

        } catch (e: Exception) {
            printer.displayLn("error: ${e.message}")
        }
    }
}
