package data.project.repository

import data.project.model.ProjectDto

interface RemoteProjectDataSource {
    suspend fun getAllProjects(): List<ProjectDto>
    suspend fun createProject(project: ProjectDto)
    suspend fun editProject(project: ProjectDto)
    suspend fun deleteProject(projectId: String)
    suspend fun getProjectById(projectId: String): ProjectDto?
}