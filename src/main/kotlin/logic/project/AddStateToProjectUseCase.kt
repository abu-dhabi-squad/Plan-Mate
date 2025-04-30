package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.DuplicateStateException
import squad.abudhabi.logic.exceptions.InvalidStateException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class AddStateToProjectUseCase(private val projectRepository: ProjectRepository){
        fun execute(projectId: String, newState: State) {
            if (newState.name.isBlank() || newState.id.isBlank()) {
                throw InvalidStateException("State name and ID cannot be blank")
            }
            val projects = projectRepository.getProjects()
            val project = projects.find { it.id == projectId }
                ?: throw ProjectNotFoundException(projectId)


            if (project.states.any { it.name.equals(newState.name, ignoreCase = true) }) {
                throw DuplicateStateException(newState.name)
            }

            val updatedProject = project.copy(states = project.states + newState)
            projectRepository.editProject(updatedProject)
        }
}