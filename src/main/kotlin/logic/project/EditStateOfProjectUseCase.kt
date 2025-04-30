package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.exceptions.ProjectStateNotFoundException
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateOfProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun editStateOfProject(projectId: String, newState: State) {
        val project = projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException(projectId)
        project.states.find { it.id == newState.id } ?: throw ProjectStateNotFoundException(newState.id)
        val updatedState = project.states.map { state -> if (state.id == newState.id) newState else state }
        projectRepository.editProject(project.copy(states = updatedState))
    }
}