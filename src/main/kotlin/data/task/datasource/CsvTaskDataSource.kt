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
            id = values[0],
            userName = values[1],
            projectId = values[2],
            stateId = values[3],
            title = values[4],
            description = values[5],
            startDate = LocalDate.parse(values[6]),
            endDate = LocalDate.parse(values[7]),
        )
    }

    private fun Task.toCsvLine() =
        "${id},${userName},${projectId},${stateId},${title},${description},${startDate},${endDate}"
}