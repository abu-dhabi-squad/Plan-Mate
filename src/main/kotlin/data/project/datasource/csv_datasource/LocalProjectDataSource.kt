package data.project.datasource.csv_datasource

import logic.model.Project

interface LocalProjectDataSource {
   suspend fun getAllProjects(): List<Project>
   suspend fun createProject(project: Project)
   suspend fun editProject(project: Project)
   suspend fun deleteProject(projectId: String)
   suspend fun getProjectById(projectId: String): Project?
}

