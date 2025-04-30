package squad.abudhabi.data.task.datasource

import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task
import java.io.File
import java.time.LocalDate

class CsvTaskDataSource(
    private val csvFileHelper: CsvFileHelper,
    private val csvFile: File,
) : TaskDataSource {
    override fun getAllTasks(): List<Task> {
        return csvFileHelper.readFile(csvFile).map (::taskFromCsvLine)
    }

    override fun getTaskById(taskId: String): Task {
        return csvFileHelper
            .readFile(csvFile)
            .let { tasks ->
                tasks.indexOfFirst { it.split(",")[0] == taskId }
                .also { it.takeIf { it != -1 } ?: throw TaskNotFoundException() }
                .let {
                    taskFromCsvLine(tasks[0])
                }
            }
    }

    override fun createTask(task: Task): Boolean {
        return csvFileHelper.appendFile(csvFile, listOf(task.toCsvLine()))
    }

    override fun editTask(task: Task): Boolean {
        return csvFileHelper.writeFile(csvFile, getTasksWithReplacedTask(task))
    }

    override fun deleteTask(taskId: String): Boolean {
        return csvFileHelper.writeFile(csvFile, getTasksWithDeletedTask(taskId))
    }

    private fun getTasksWithReplacedTask(task: Task): List<String> {
        return csvFileHelper.readFile(csvFile).let { tasks ->
            tasks
                .indexOfFirst { it.split(",")[0] == task.id }
                .also { it.takeIf { it != -1 } ?: throw TaskNotFoundException() }
                .let {
                    tasks.subList(0, it) + task.toCsvLine() + tasks.subList(it + 1, tasks.size)
                }
        }
    }

    private fun getTasksWithDeletedTask(taskId: String): List<String> {
        return csvFileHelper.readFile(csvFile).let { tasks ->
            tasks
                .indexOfFirst { it.split(",")[0] == taskId }
                .also { it.takeIf { it != -1 } ?: throw TaskNotFoundException() }
                .let {
                    tasks.subList(0, it) + tasks.subList(it + 1, tasks.size)
                }
        }
    }

    private fun taskFromCsvLine(taskLine: String): Task {
        val values = taskLine.split(",")
        return Task(
            id = values[TaskColumnIndex.ID.ordinal],
            userName = values[TaskColumnIndex.USERNAME.ordinal],
            projectId = values[TaskColumnIndex.PROJECT_ID.ordinal],
            stateId = values[TaskColumnIndex.STATE_ID.ordinal],
            title = values[TaskColumnIndex.TITLE.ordinal],
            description = values[TaskColumnIndex.DESCRIPTION.ordinal],
            startDate = LocalDate.parse(values[TaskColumnIndex.START_DATE.ordinal]),
            endDate = LocalDate.parse(values[TaskColumnIndex.END_DATE.ordinal]),
        )
    }

    private fun Task.toCsvLine(): String {
        MutableList(TaskColumnIndex.entries.size) { "" }.let {
            it[TaskColumnIndex.ID.ordinal] = id
            it[TaskColumnIndex.USERNAME.ordinal] = userName
            it[TaskColumnIndex.PROJECT_ID.ordinal] = projectId
            it[TaskColumnIndex.STATE_ID.ordinal] = stateId
            it[TaskColumnIndex.TITLE.ordinal] = title
            it[TaskColumnIndex.DESCRIPTION.ordinal] = description
            it[TaskColumnIndex.START_DATE.ordinal] = startDate.toString()
            it[TaskColumnIndex.END_DATE.ordinal] = endDate.toString()
            return it.joinToString(",")
        }
    }
}