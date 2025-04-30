package squad.abudhabi.data.project.repository

import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override fun getAllProjects(): List<Project> {
        return projectDataSource.readProjects()
    }

    override fun addProject(project: Project) {
        projectDataSource.writeProject(project)
    }

    override fun editProject(project: Project) {
        projectDataSource.editProject(project)
    }

    override fun deleteProject(projectId: String) {
        projectDataSource.deleteProject(projectId)
    }

    override fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProject(projectId)
    }
}