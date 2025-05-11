package data.project.repository

import data.project.mapper.ProjectMapper
import logic.model.Project
import logic.repository.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: RemoteProjectDataSource,
    private val projectMapper: ProjectMapper

) : ProjectRepository {

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects().map { projectMapper.dtoToProject(it) }
    }

    override suspend fun addProject(project: Project) {
        projectDataSource.createProject(projectMapper.projectToDto(project))
    }

    override suspend fun editProject(project: Project) {
        projectDataSource.editProject(projectMapper.projectToDto(project))
    }

    override suspend fun deleteProjectById(projectId: UUID) {
        projectDataSource.deleteProject(projectId.toString())
    }

    override suspend fun getProjectById(projectId: UUID): Project? {
        return projectDataSource.getProjectById(projectId.toString())?.let { projectMapper.dtoToProject(it) }
    }

}