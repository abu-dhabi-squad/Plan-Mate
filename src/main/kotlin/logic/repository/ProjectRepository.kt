package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Project

interface ProjectRepository {
    fun getProjects() : List<Project>
    fun addProject(project: Project) : Boolean
    fun editProject(project: Project) : Boolean
    fun deleteProject(projectId: String) : Boolean
}