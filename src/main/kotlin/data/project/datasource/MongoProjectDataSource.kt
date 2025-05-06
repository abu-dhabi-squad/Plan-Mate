package data.project.datasource

import com.mongodb.client.MongoCollection
import data.project.ProjectMapper
import org.bson.Document
import squad.abudhabi.logic.model.Project


class MongoProjectDataSource(private val collection: MongoCollection<Document>, private val mapper: ProjectMapper) :
    ProjectDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return collection.find().map { doc -> mapper.documentToProject(doc) }.toList()
    }

    override suspend fun createProject(project: Project) {
        val doc = mapper.projectToDocument(project)
        collection.insertOne(doc)
    }

    override suspend fun editProject(project: Project) {
        val updateDoc = Document(
            "\$set", Document(ProjectMapper.PROJECT_NAME_FIELD, project.projectName)
                .append(ProjectMapper.STATES_FIELD, project.states)
        )
        collection.updateOne(Document(ProjectMapper.ID_FIELD, project.id), updateDoc)
    }

    override suspend fun deleteProject(projectId: String) {
        collection.deleteOne(Document(ProjectMapper.ID_FIELD, projectId))
    }

    override suspend fun getProjectById(projectId: String): Project? {
        val doc = collection.find(Document(ProjectMapper.ID_FIELD, projectId)).first() ?: return null
        return mapper.documentToProject(doc)
    }

}