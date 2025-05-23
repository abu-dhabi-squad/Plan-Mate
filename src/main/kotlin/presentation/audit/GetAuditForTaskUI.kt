package presentation.audit

import logic.audit.GetAuditUseCase
import logic.model.Audit
import logic.model.Project
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import presentation.utils.extensions.showAuditLogs
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetAuditForTaskUI(
    private val printer: Printer,
    private val promptUtils: PromptUtils,
    private val getAuditUseCase: GetAuditUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
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
        showTaskAudit(projects[projectIndex])
    }

    private suspend fun showTaskAudit(project: Project) {
        val tasks = try {
            getTasksByProjectIdUseCase(project.projectId)
        } catch (exception: Exception) {
            printer.displayLn("\nFailed to fetch tasks: ${exception.message}")
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
        val taskIndex = promptUtils.promptSelectionIndex("\nEnter task number", tasks.size)
        showAuditLogs(tasks[taskIndex].taskId)
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
