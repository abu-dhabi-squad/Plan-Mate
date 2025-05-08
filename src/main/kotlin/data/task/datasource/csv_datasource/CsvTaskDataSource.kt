package data.task.datasource.csv_datasource

import data.task.parser.TaskParser
import data.utils.filehelper.FileHelper
import logic.model.Task

class CsvTaskDataSource(
    private val csvFileHelper: FileHelper,
    private val csvFileName: String,
    private val csvTaskParser: TaskParser,
) : LocalTaskDataSource {
    override suspend fun getAllTasks(): List<Task> {
        return csvFileHelper.readFile(csvFileName).map { csvTaskParser.getTaskFromCsvLine(it) }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return getAllTasks().find { it.id.toString() == taskId }
    }

    override suspend fun getTaskByProjectId(projectId: String): List<Task> {
        return getAllTasks().filter { it.projectId == projectId }
    }

    override suspend fun createTask(task: Task) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvTaskParser.getCsvLineFromTask(task)))
    }

    override suspend fun editTask(task: Task) {
        return csvFileHelper.writeFile(
            csvFileName,
            getTasksWithReplacedTask(task).parseToCsvLines()
        )
    }

    override suspend fun deleteTask(taskId: String) {
        return csvFileHelper.writeFile(
            csvFileName,
            getTasksWithDeletedTask(taskId).parseToCsvLines()
        )
    }

    private suspend fun getTasksWithReplacedTask(task: Task): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id == task.id }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    private suspend fun getTasksWithDeletedTask(taskId: String): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id.toString() == taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    private fun List<Task>.parseToCsvLines(): List<String> {
        return map { csvTaskParser.getCsvLineFromTask(it) }
    }
}