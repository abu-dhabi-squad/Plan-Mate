package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Project

interface ProjectRepository {
  suspend fun getAllProjects(): List<Project>
  suspend fun addProject(project: Project)
  suspend fun editProject(project: Project)
  suspend fun deleteProject(projectId: String)
  suspend fun getProjectById(projectId: String): Project?
}