package data.project.datasource

import logic.model.Project

interface ProjectDataSource {
    fun getAllProjects(): List<Project>
    fun createProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(projectId: String)
    fun getProjectById(projectId: String): Project?
}