package squad.abudhabi.data.task.datasource

import squad.abudhabi.data.task.parser.CsvTaskParser
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.logic.model.Task

class CsvTaskDataSource(
    private val csvFileHelper: CsvFileHelper,
    private val csvFileName: String,
    private val csvTaskParser: CsvTaskParser,
) : TaskDataSource {
    override fun getAllTasks(): List<Task> {
        return csvFileHelper.readFile(csvFileName).map { csvTaskParser.getTaskFromCsvLine(it) }
    }

    override fun getTaskById(taskId: String): Task? {
        return getAllTasks().find { it.id == taskId }
    }

    override fun getTaskByProjectId(projectId: String): List<Task> {
        return getAllTasks().filter { it.projectId == projectId }
    }

    override fun createTask(task: Task) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvTaskParser.getCsvLineFromTask(task)))
    }

    override fun editTask(task: Task) {
        return csvFileHelper.writeFile(csvFileName, getTasksWithReplacedTask(task))
    }

    override fun deleteTask(taskId: String) {
        return csvFileHelper.writeFile(csvFileName, getTasksWithDeletedTask(taskId))
    }

    private fun getTasksWithReplacedTask(task: Task): List<String> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id == task.id }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task + tasks.subList(taskIndex + 1, tasks.size))
                    .map { csvTaskParser.getCsvLineFromTask(it) }
            }
        }
    }

    private fun getTasksWithDeletedTask(taskId: String): List<String> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id == taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
                    .map { csvTaskParser.getCsvLineFromTask(it) }
            }
        }
    }
}