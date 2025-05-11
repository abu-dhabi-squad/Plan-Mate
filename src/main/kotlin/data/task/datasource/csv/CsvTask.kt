package data.task.datasource.csv

import data.task.repository.LocalTaskDataSource
import data.utils.filehelper.FileHelper
import logic.model.Task
import java.util.*

class CsvTask(
    private val csvFileHelper: FileHelper,
    private val csvFileName: String,
    private val csvTaskParser: CsvTaskParser,
) : LocalTaskDataSource {

    override fun getAllTasks(): List<Task> {
        return csvFileHelper.readFile(csvFileName).map { csvTaskParser.getTaskFromCsvLine(it) }
    }

    override fun getTaskById(taskId: UUID): Task? {
        return getAllTasks().find { it.taskId == taskId }
    }

    override fun getTaskByProjectId(projectId: UUID): List<Task> {
        return getAllTasks().filter { it.projectId == projectId }
    }

    override fun createTask(task: Task) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvTaskParser.getCsvLineFromTask(task)))
    }

    override fun editTask(task: Task) {
        return csvFileHelper.writeFile(
            csvFileName,
            getTasksWithReplacedTask(task).parseToCsvLines()
        )
    }

    override fun deleteTask(taskId: String) {
        return csvFileHelper.writeFile(
            csvFileName,
            getTasksWithDeletedTask(taskId).parseToCsvLines()
        )
    }

    private fun getTasksWithReplacedTask(task: Task): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.taskId == task.taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    private fun getTasksWithDeletedTask(taskId: String): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.taskId.toString() == taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    private fun List<Task>.parseToCsvLines(): List<String> {
        return map { csvTaskParser.getCsvLineFromTask(it) }
    }
}