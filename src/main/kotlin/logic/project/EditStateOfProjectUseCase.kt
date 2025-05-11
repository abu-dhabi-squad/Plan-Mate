package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.exceptions.ProjectStateNotFoundException
import logic.model.TaskState
import logic.repository.ProjectRepository
import java.util.*

class EditStateOfProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: UUID, newTaskState: TaskState) {
        val project = projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
        project.taskStates.find { it.stateId == newTaskState.stateId } ?: throw ProjectStateNotFoundException()
        val updatedState =
            project.taskStates.map { state -> if (state.stateId == newTaskState.stateId) newTaskState else state }
        projectRepository.editProject(project.copy(taskStates = updatedState))
    }
}