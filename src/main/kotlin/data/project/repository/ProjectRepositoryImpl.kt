package data.project.repository

import data.project.mapper.ProjectMapper
import logic.model.Project
import logic.repository.ProjectRepository
import data.utils.BaseRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectRepositoryImpl(
    private val projectDataSource: RemoteProjectDataSource,
    private val projectMapper: ProjectMapper
) : ProjectRepository, BaseRepository() {

    override suspend fun getAllProjects(): List<Project> {
        return wrapResponse {
            projectDataSource.getAllProjects().map { projectMapper.dtoToProject(it) }
        }
    }

    override suspend fun addProject(project: Project) {
        wrapResponse {
            projectDataSource.createProject(projectMapper.projectToDto(project))
        }
    }

    override suspend fun editProject(project: Project) {
        wrapResponse {
            projectDataSource.editProject(projectMapper.projectToDto(project))
        }
    }

    override suspend fun deleteProjectById(projectId: Uuid) {
        wrapResponse {
            projectDataSource.deleteProject(projectId.toString())
        }
    }

    override suspend fun getProjectById(projectId: Uuid): Project? {
        return wrapResponse {
            projectDataSource.getProjectById(projectId.toString())
                ?.let { projectMapper.dtoToProject(it) }
        }
    }

}