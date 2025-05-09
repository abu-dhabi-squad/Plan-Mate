package data.task.datasource.csv_datasource

import logic.validation.DateParser
import logic.model.Task
import java.util.UUID


class CsvTaskParser(
    private val dateParser: DateParser
)  {

     fun getCsvLineFromTask(task: Task): String {
        return "${task.id},${task.userName},${task.projectId},${task.stateId},${task.title},${task.description},${
            dateParser.getStringFromDate(
                task.startDate
            )
        },${dateParser.getStringFromDate(task.startDate)}"
    }

     fun getTaskFromCsvLine(taskLine: String): Task {
        return taskLine.split(",")
            .let {
                Task(
                    id = UUID.fromString(it[ID]),
                    userName = it[USERNAME],
                    projectId = it[PROJECT_ID],
                    stateId = it[STATE_ID],
                    title = it[TITLE],
                    description = it[DESCRIPTION],
                    startDate = dateParser.parseDateFromString(it[START_DATE]),
                    endDate = dateParser.parseDateFromString(it[END_DATE]),
                )
            }
    }

    private companion object {
        const val ID = 0
        const val USERNAME = 1
        const val PROJECT_ID = 2
        const val STATE_ID = 3
        const val TITLE = 4
        const val DESCRIPTION = 5
        const val START_DATE = 6
        const val END_DATE = 7
    }
}