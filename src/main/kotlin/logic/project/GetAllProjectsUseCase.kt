package logic.project

import logic.exceptions.NoProjectsFoundException
import logic.model.Project
import logic.repository.ProjectRepository

class GetAllProjectsUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(): List<Project> {
        return projectRepository.getAllProjects()
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectsFoundException()
    }
}