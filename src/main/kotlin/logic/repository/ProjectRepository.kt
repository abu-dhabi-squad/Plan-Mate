package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Project

interface ProjectRepository {
    fun getAllProjects(): List<Project>
    fun addProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(projectId: String)
    fun getProjectById(projectId: String): Project?
}