package data.task.mapper

import org.bson.Document
import squad.abudhabi.logic.model.Task
import java.time.LocalDate
import java.util.*

class TaskMapper {

    fun documentToTask(doc: Document): Task {
        return Task(
            id = UUID.fromString(doc.getString(TASK_ID_FIELD)),
            userName = doc.getString(USERNAME_FIELD),
            projectId = doc.getString(PROJECT_ID_FIELD),
            stateId = doc.getString(STATE_ID_FIELD),
            title = doc.getString(TITLE_FIELD),
            description = doc.getString(DESCRIPTION_FIELD),
            startDate = LocalDate.parse(doc.getString(START_DATE)),
            endDate = LocalDate.parse(doc.getString(END_DATE))
        )
    }

    fun taskToDocument(task: Task): Document {
        return Document(TASK_ID_FIELD, task.id.toString())
            .append(USERNAME_FIELD, task.userName)
            .append(PROJECT_ID_FIELD, task.projectId)
            .append(STATE_ID_FIELD, task.stateId)
            .append(TITLE_FIELD, task.title)
            .append(DESCRIPTION_FIELD, task.description)
            .append(START_DATE, task.startDate.toString())
            .append(END_DATE, task.endDate.toString())
    }

    companion object {
        const val TASK_ID_FIELD = "id"
        const val USERNAME_FIELD = "username"
        const val PROJECT_ID_FIELD = "projectId"
        const val STATE_ID_FIELD = "stateId"
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
    }


}