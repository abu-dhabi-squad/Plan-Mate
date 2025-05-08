package logic.repository

import logic.model.Project

interface ProjectRepository {
    fun getAllProjects(): List<Project>
    fun addProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(projectId: String)
    fun getProjectById(projectId: String): Project?
}