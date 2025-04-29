package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.InvalidProjectNameException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(projectName: String):Boolean {
        if (projectName.isBlank()) {
            throw InvalidProjectNameException()
        }

        val newProject = Project(
            id = UUID.randomUUID().toString(),
            projectName = projectName,
            states = emptyList()
        )

        return projectRepository.addProject(newProject)
    }

}