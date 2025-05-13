package logic.repository

import logic.model.Project
import java.util.*

interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun addProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProjectById(projectId: UUID)
    suspend fun getProjectById(projectId: UUID): Project?
}