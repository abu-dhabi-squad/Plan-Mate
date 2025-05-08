package logic.repository

import logic.model.Project

interface ProjectRepository {
  suspend fun getAllProjects(): List<Project>
  suspend fun addProject(project: Project)
  suspend fun editProject(project: Project)
  suspend fun deleteProjectById(projectId: String)
  suspend fun getProjectById(projectId: String): Project?
}