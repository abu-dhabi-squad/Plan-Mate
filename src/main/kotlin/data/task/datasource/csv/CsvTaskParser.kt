package data.task.datasource.csv

import logic.model.Task
import logic.utils.DateParser
import java.util.UUID


class CsvTaskParser(
    private val dateParser: DateParser
) {

    fun getCsvLineFromTask(task: Task): String {
        return "${task.taskId},${task.username},${task.projectId},${task.taskStateId},${task.title},${task.description},${
            dateParser.getStringFromDate(
                task.startDate
            )
        },${dateParser.getStringFromDate(task.startDate)}"
    }

    fun getTaskFromCsvLine(taskLine: String): Task {
        return taskLine.split(",")
            .let {
                Task(
                    taskId = UUID.fromString(it[ID]),
                    username = it[USERNAME],
                    projectId = UUID.fromString(it[PROJECT_ID]),
                    taskStateId = UUID.fromString(it[STATE_ID]),
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