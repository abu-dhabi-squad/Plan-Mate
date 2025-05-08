package data.project.repository

import data.project.mapper.ProjectMapper
import logic.model.Project
import logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: RemoteProjectDataSource,
    private val projectMapper: ProjectMapper

) : ProjectRepository {

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects().map { projectMapper.mapDtoToProject(it) }
    }

    override suspend fun addProject(project: Project) {
        projectDataSource.createProject(projectMapper.mapProjectToDto(project))
    }

    override suspend fun editProject(project: Project) {
        projectDataSource.editProject(projectMapper.mapProjectToDto(project))
    }

    override suspend fun deleteProject(projectId: String) {
        projectDataSource.deleteProject(projectId)
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return projectDataSource.getProjectById(projectId)?.let { projectMapper.mapDtoToProject(it) }
    }

}