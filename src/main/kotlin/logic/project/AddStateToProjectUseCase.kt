package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.DuplicateStateException
import squad.abudhabi.logic.exceptions.InvalidStateException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class AddStateToProjectUseCase(private val projectRepository: ProjectRepository){
        fun execute(projectId: String, newState: State) {
            TODO()
        }
}