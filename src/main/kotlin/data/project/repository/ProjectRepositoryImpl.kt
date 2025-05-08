package data.project.repository

import data.project.mapper.MongoProjectMapper
import logic.model.Project
import logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: RemoteProjectDataSource,
    private val mongoProjectMapper: MongoProjectMapper

) : ProjectRepository {

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects().map { mongoProjectMapper.mapDtoToProject(it) }
    }

    override suspend fun addProject(project: Project) {
        projectDataSource.createProject(mongoProjectMapper.mapProjectToDto(project))
    }

    override suspend fun editProject(project: Project) {
        projectDataSource.editProject(mongoProjectMapper.mapProjectToDto(project))
    }

    override suspend fun deleteProjectById(projectId: String) {
        projectDataSource.deleteProject(projectId)
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProjectById(projectId)?.let { mongoProjectMapper.mapDtoToProject(it) }
    }

}