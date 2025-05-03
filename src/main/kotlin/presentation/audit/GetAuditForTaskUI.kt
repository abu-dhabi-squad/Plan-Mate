package presentation.audit

import GetAuditUseCase
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
            printer.display("Failed to fetch projects: ${e.message}")
            return
        }

        if (projects.isEmpty()) {
            printer.display("No projects available.")
            return
        }

        printer.display("=== Available Projects ===")
        projects.forEachIndexed { index, project ->
            printer.display("${index + 1}. ${project.projectName}")
        }

        printer.display("Enter project number: ")
        val projectChoice = reader.readInt()
        if (projectChoice == null) {
            printer.display("Input cannot be empty.")
            return
        }
        if (projectChoice !in 1..projects.size) {
            printer.display("Input cannot be out projects range.")
            return
        }
        val selectedProject = projects.getOrNull(projectChoice - 1)
        if (selectedProject == null) {
            printer.display("Invalid project selection.")
            return
        }

        showTaskAudit(selectedProject)
    }

    private fun showTaskAudit(project: Project) {

        val tasks = try {
            getTasksByProjectIdUseCase(project.id)
        } catch (e: Exception) {
            printer.display("Failed to fetch tasks: ${e.message}")
            return
        }

        if (tasks.isEmpty()) {
            printer.display("No tasks available in this project.")
            return
        }

        printer.display("=== Tasks in Project: ${project.projectName} ===")
        tasks.forEachIndexed { index, task ->
            printer.display("${index + 1}. ${task.title}")
        }

        printer.display("Enter task number: ")
        val taskChoice = reader.readInt()
        if (taskChoice == null) {
            printer.display("Input cannot be empty.")
            return
        }
        if (taskChoice !in 1..tasks.size) {
            printer.display("Input cannot be out tasks range.")
            return
        }
        val selectedTask = tasks.getOrNull(taskChoice - 1)
        if (selectedTask == null) {
            printer.display("Invalid task selection.")
            return
        }

        showAuditLogs(selectedTask.id)
    }

    private fun showAuditLogs(entityId: String) {
        try {
            val audits: List<Audit> = getAuditUseCase(entityId)
            if (audits.isEmpty()) {
                printer.display("No audit logs found for this task.")
                return
            }

            printer.display("=== Audit Logs for Task ===")
            audits.forEachIndexed { index, audit ->
                printer.display("${index + 1}. Entity: ${audit.entityId}, Created By: ${audit.createdBy}, Date: ${audit.date}")
                if (audit.oldState.isEmpty()) printer.display("New state set as ${audit.newState}")
                else printer.display("Changed from ${audit.oldState} to ${audit.newState}")
            }

        } catch (e: Exception) {
            printer.display("error: ${e.message}")
        }
    }
}
