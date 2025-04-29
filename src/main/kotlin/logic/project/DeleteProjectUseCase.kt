package squad.abudhabi.logic.project

import squad.abudhabi.logic.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(projectId: String) {
        TODO()
    }
}