package data.project.datasource.mongo_datasource

import com.mongodb.client.MongoCollection
import data.project.mapper.ProjectMapper
import data.project.datasource.ProjectDataSource
import org.bson.Document
import logic.model.Project

class MongoProjectDataSource(private val collection: MongoCollection<Document>, private val mapper: ProjectMapper) :
    ProjectDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return collection.find().map { doc -> mapper.documentToProject(doc) }.toList()
//        collection.find().iterator().asSequence().map { mapper.documentToProject(it) }.toList()
    }

    override suspend fun createProject(project: Project) {
        val doc = mapper.projectToDocument(project)
        collection.insertOne(doc)
    }

override suspend fun editProject(project: Project) {
    val statesDocs = project.states.map { state ->
        Document()
            .append(ProjectMapper.STATE_ID_FIELD, state.id)
            .append(ProjectMapper.STATE_NAME_FIELD, state.name)
    }

    val updateDoc = Document(
        "\$set", Document(ProjectMapper.PROJECT_NAME_FIELD, project.projectName)
            .append(ProjectMapper.STATES_FIELD, statesDocs) // ✅
    )

    collection.updateOne(Document(ProjectMapper.ID_FIELD, project.id.toString()), updateDoc)
}

    override suspend fun deleteProject(projectId: String) {
        collection.deleteOne(Document(ProjectMapper.Companion.ID_FIELD, projectId))
    }

    override suspend fun getProjectById(projectId: String): Project? {
        val doc = collection.find(Document(ProjectMapper.Companion.ID_FIELD, projectId)).first() ?: return null
        return mapper.documentToProject(doc)
    }

}