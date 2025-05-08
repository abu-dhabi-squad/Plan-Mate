package data.project.datasource.mongo_datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.project.model.ProjectDto
import data.project.repository.RemoteProjectDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document

class MongoProjectDataSource(
    private val projectCollection: MongoCollection<ProjectDto>,
) : RemoteProjectDataSource {

    override suspend fun getAllProjects(): List<ProjectDto> {
        return projectCollection.find().toList()
    }

    override suspend fun createProject(project: ProjectDto) {
        projectCollection.insertOne(project)
    }

    override suspend fun editProject(project: ProjectDto) {
        val statesDocs = project.states.map { state ->
            Document()
                .append(STATE_ID_FIELD, state.id)
                .append(STATE_NAME_FIELD, state.name)
        }

        val updateDoc = Document(
            "\$set", Document()
                .append(PROJECT_NAME_FIELD, project.projectName)
                .append(STATES_FIELD, statesDocs)
        )

        projectCollection.updateOne(
            Filters.eq(PROJECT_ID_FIELD, project.id),
            updateDoc
        )
    }

    override suspend fun deleteProject(projectId: String) {
        projectCollection.deleteOne(Filters.eq(PROJECT_ID_FIELD, projectId))
    }

    override suspend fun getProjectById(projectId: String): ProjectDto? {
        return projectCollection.find(Filters.eq(PROJECT_ID_FIELD, projectId))
            .firstOrNull()
    }

    companion object {
        private const val STATE_ID_FIELD = "id"
        private const val STATE_NAME_FIELD = "name"
        private const val PROJECT_ID_FIELD = "id"
        private const val PROJECT_NAME_FIELD = "projectName"
        private const val STATES_FIELD = "states"
    }
}
