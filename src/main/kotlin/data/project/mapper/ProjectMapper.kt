package data.project.mapper

import data.task.mapper.TaskMapper.Companion.STATE_ID_FIELD
import org.bson.Document
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import java.util.UUID

class ProjectMapper {
    fun documentToProject(doc: Document): Project {
        val statesDocs = doc.getList(STATES_FIELD, Document::class.java)
        val states = statesDocs.map { stateDoc ->
            State(
                id = stateDoc.getString(STATE_ID_FIELD),
                name = stateDoc.getString(STATE_NAME_FIELD)
            )
        }
        return Project(
            id = UUID.fromString(doc.getString(ID_FIELD)),
            projectName = doc.getString(PROJECT_NAME_FIELD),
            states = states
        )
    }

    fun projectToDocument(project: Project): Document {
        val statesDocs = project.states.map { state ->
            Document()
                .append(STATE_ID_FIELD, state.id)
                .append(STATE_NAME_FIELD, state.name)
        }

        return Document(ID_FIELD, project.id.toString())
            .append(PROJECT_NAME_FIELD, project.projectName)
            .append(STATES_FIELD, statesDocs)
    }

    companion object {
        const val STATE_ID_FIELD = "id"
        const val STATE_NAME_FIELD = "name"
        const val ID_FIELD = "id"
        const val PROJECT_NAME_FIELD = "projectName"
        const val STATES_FIELD = "states"
    }
}