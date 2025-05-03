package squad.abudhabi.logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository
import java.util.UUID

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectName: String, states: List<State>) : String{
        val newProject = Project(
            projectName = projectName,
            states = states
        )
        projectRepository.addProject(newProject)

        return newProject.id
    }
}