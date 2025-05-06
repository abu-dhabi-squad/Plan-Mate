package data.task.parser

import logic.model.Task

interface TaskParser {
    fun getCsvLineFromTask(task: Task): String
    fun getTaskFromCsvLine(taskLine: String): Task
}