package squad.abudhabi.logic.project

import squad.abudhabi.logic.repository.ProjectRepository

class AddStateToProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(projectId: String, stateName: String) {
        TODO()
    }

}