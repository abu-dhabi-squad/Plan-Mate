package data.project

import org.bson.Document
import squad.abudhabi.logic.model.Project
import java.util.UUID
import kotlin.jvm.java

class ProjectMapper {
    fun documentToProject(doc: Document): Project {
        return Project(
            id = UUID.fromString(doc.getString(ID_FIELD)),
            projectName = doc.getString(PROJECT_NAME_FIELD),
            states = doc.getList(STATES_FIELD, squad.abudhabi.logic.model.State::class.java)
        )
    }

    fun projectToDocument(project: Project): Document {
        return Document(ID_FIELD, project.id.toString())
            .append(PROJECT_NAME_FIELD, project.projectName)
            .append(STATES_FIELD, project.states)
    }

    companion object {
        const val ID_FIELD = "id"
        const val PROJECT_NAME_FIELD = "projectName"
        const val STATES_FIELD = "states"
    }
}
