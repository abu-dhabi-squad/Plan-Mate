package logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.exceptions.ProjectStateNotFoundException
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

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