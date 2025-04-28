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
        if (projectDataSource.readProjects().isEmpty()) throw NoProjectsFoundException()
        return projectDataSource.readProjects()
    }

    override fun addProject(project: Project): Boolean {
        val newProjects = projectDataSource.readProjects().toMutableList()
        if (newProjects.isEmpty()){
            return projectDataSource.writeProjects(listOf(project))
        }
        newProjects.add(project)
        return projectDataSource.writeProjects(newProjects)
    }

    override fun editProject(project: Project): Boolean {
        val projects = projectDataSource.readProjects().toMutableList()

        if (projectDataSource.readProjects().isEmpty()) throw NoProjectsFoundException()
        if(!projects.isContainProject(project)) throw ProjectNotInListException()

        projects.map { currentProject -> if(currentProject.id == project.id) project else currentProject }
        return projectDataSource.writeProjects(projects)
    }

    private fun List<Project>.isContainProject(project: Project) : Boolean {
        this.forEach { currentProject ->
            if(currentProject.id == project.id) return true
        }
        return false
    }
}