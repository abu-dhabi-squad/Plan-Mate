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
        val projects = projectDataSource.readProjects().toMutableList()
        projects.add(project)
        return projectDataSource.writeProjects(projects)
    }

    override fun editProject(project: Project): Boolean {
        val projects = projectDataSource.readProjects().toMutableList()

        projects.map { currentProject -> currentProject.isEqualProject(project)}
        return projectDataSource.writeProjects(projects)
    }

    private fun Project.isEqualProject(project : Project): Project{
        if (this.id == project.id) return project else return this
    }

}