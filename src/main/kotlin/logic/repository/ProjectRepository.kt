package logic.repository

import logic.model.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun addProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProjectById(projectId: Uuid)
    suspend fun getProjectById(projectId: Uuid): Project?
}