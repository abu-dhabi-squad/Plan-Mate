package data.task.parser

import logic.validation.DateParser
import logic.model.Task

class CsvTaskParser(
    private val dateParser: DateParser
) : TaskParser {

    override fun getCsvLineFromTask(task: Task): String {
        return "${task.id},${task.userName},${task.projectId},${task.stateId},${task.title},${task.description},${dateParser.getStringFromDate(task.startDate)},${dateParser.getStringFromDate(task.startDate)}"
    }

    override fun getTaskFromCsvLine(taskLine: String): Task {
        return taskLine.split(",")
            .let {
                Task(
                    id = it[TaskColumnIndex.ID],
                    userName = it[TaskColumnIndex.USERNAME],
                    projectId = it[TaskColumnIndex.PROJECT_ID],
                    stateId = it[TaskColumnIndex.STATE_ID],
                    title = it[TaskColumnIndex.TITLE],
                    description = it[TaskColumnIndex.DESCRIPTION],
                    startDate = dateParser.parseDateFromString(it[TaskColumnIndex.START_DATE]),
                    endDate = dateParser.parseDateFromString(it[TaskColumnIndex.END_DATE]),
                )
            }
    }
}