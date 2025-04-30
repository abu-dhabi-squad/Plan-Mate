package squad.abudhabi.data.task.parser

import squad.abudhabi.logic.model.Task

interface TaskParser {
    fun getCsvLineFromTask(task: Task): String
    fun getTaskFromCsvLine(taskLine: String): Task
}