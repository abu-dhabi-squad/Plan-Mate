package logic.project

import logic.exceptions.DuplicateStateException
import logic.exceptions.ProjectNotFoundException
import logic.model.State
import logic.repository.ProjectRepository

class AddStateToProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: String, newState: State) {
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()

        if (project.states.isNotEmpty()) {
            project.states.find { !it.name.equals(newState.name, ignoreCase = true) }
                ?: throw DuplicateStateException(newState.name)
        }

        val updatedProject = project.copy(states = project.states + newState)
        projectRepository.editProject(updatedProject)
    }
}