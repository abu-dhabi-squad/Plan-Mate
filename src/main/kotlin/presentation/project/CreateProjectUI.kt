package presentation.project

import logic.audit.CreateAuditUseCase
import logic.project.CreateProjectUseCase
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.model.State
import logic.user.GetLoggedUserUseCase


class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val inputReader: InputReader,
    private val printer: Printer,
    private val createAuditUseCase: CreateAuditUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
): UiLauncher {

    override suspend fun launchUi() {
        printer.display("\nEnter project name: ")
        val projectName = inputReader.readString()?.takeIf { it.isNotBlank() }
        if (projectName == null) {
            printer.displayLn("\nProject name cannot be empty.")
            return
        }

        printer.display("\nEnter number of states: ")
        val stateCount = inputReader.readInt()
        if (stateCount == null || stateCount < 0) {
            printer.displayLn("\nInvalid number of states.")
            return
        }

        val states = mutableListOf<State>()
        for (i in 1..stateCount) {
            printer.display("\nEnter name for state #$i: ")
            val stateName = inputReader.readString()?.takeIf { it.isNotBlank() }
            if (stateName == null) {
                printer.displayLn("\nState name cannot be empty.")
                return
            }
            states.add(State(name = stateName))
        }

        try {
            val newProject = Project(projectName = projectName, states = states)
            createProjectUseCase(newProject)
            createAuditUseCase(
                Audit(
                    createdBy = getLoggedUserUseCase().username,
                    entityType = EntityType.PROJECT,
                    entityId = newProject.id.toString(),
                    oldState = "",
                    newState = "Created"
                )
            )
            printer.displayLn("\nProject '$projectName' created with ${states.size} state(s).")
        } catch (e: Exception) {
            printer.displayLn("\nError: ${e.message}")
        }
    }
}