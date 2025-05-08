package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.exceptions.ProjectStateNotFoundException
import logic.model.State
import logic.repository.ProjectRepository

class EditStateOfProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectId: String, newState: State) {
        val project = projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
        project.states.find { it.id == newState.id } ?: throw ProjectStateNotFoundException()
        val updatedState = project.states.map { state -> if (state.id == newState.id) newState else state }
        projectRepository.editProject(project.copy(states = updatedState))
    }
}