package data.project.repository

import data.project.datasource.ProjectDataSource
import logic.model.Project
import logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override fun addProject(project: Project) {
        projectDataSource.createProject(project)
    }

    override fun editProject(project: Project) {
        projectDataSource.editProject(project)
    }

    override fun deleteProject(projectId: String) {
        projectDataSource.deleteProject(projectId)
    }

    override fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProjectById(projectId)
    }
}