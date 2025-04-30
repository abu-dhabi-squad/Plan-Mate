package squad.abudhabi.data.project.repository

import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override fun getProjects(): List<Project> {
        return projectDataSource.readProjects()
    }

    override fun addProject(project: Project): Boolean {
        return projectDataSource.writeProject(project)
    }

    override fun editProject(project: Project): Boolean {
        return projectDataSource.editProject(project)
    }

    override fun deleteProject(project: Project): Boolean {
        return projectDataSource.deleteProject(project)
    }

    override fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProject(projectId)
    }

}