package squad.abudhabi.data.project.repository

import squad.abudhabi.data.Exceptions.NoProjectsFoundException
import squad.abudhabi.data.Exceptions.ProjectNotInListException
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override fun getProjects(): List<Project> {
        TODO()
    }

    override fun addProject(project: Project): Boolean {
        TODO()
    }

    override fun editProject(project: Project): Boolean {
        TODO()
    }
}