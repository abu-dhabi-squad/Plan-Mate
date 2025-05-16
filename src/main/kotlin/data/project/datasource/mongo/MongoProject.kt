package data.project.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.project.model.ProjectDto
import data.project.model.StateDto
import data.project.repository.RemoteProjectDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document

class MongoProject(
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
                .append(StateDto::_id.name, state._id)
                .append(StateDto::name.name, state.name)
        }

        val updateDoc = Document(
            "\$set", Document()
                .append(ProjectDto::projectName.name, project.projectName)
                .append(ProjectDto::states.name, statesDocs)
        )

        projectCollection.updateOne(
            Filters.eq(ProjectDto::_id.name, project._id),
            updateDoc
        )
    }

    override suspend fun deleteProject(projectId: String) {
        projectCollection.deleteOne(Filters.eq(ProjectDto::_id.name, projectId))
    }

    override suspend fun getProjectById(projectId: String): ProjectDto? {
        return projectCollection.find(Filters.eq(ProjectDto::_id.name, projectId))
            .firstOrNull()
    }
}
