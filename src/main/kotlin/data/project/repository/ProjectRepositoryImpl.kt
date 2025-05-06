package data.project.repository

import data.project.datasource.ProjectDataSource
import logic.model.Project
import logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override suspend fun addProject(project: Project) {
        projectDataSource.createProject(project)
    }

    override suspend fun editProject(project: Project) {
        projectDataSource.editProject(project)
    }

    override suspend fun deleteProject(projectId: String) {
        projectDataSource.deleteProject(projectId)
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProjectById(projectId)
    }
}