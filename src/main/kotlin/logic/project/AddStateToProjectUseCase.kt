package logic.project

import squad.abudhabi.logic.exceptions.DuplicateStateException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

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