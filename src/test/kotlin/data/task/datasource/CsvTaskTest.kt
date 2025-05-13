package data.task.datasource

import com.google.common.truth.Truth.assertThat
import data.task.datasource.csv.CsvTask
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import data.task.datasource.csv.CsvTaskParser
import data.utils.filehelper.CsvFileHelper
import java.util.UUID

class CsvTaskTest {
    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var csvTaskParser: CsvTaskParser
    private lateinit var csvTask: CsvTask

    @BeforeEach
    fun setup() {
        csvFileHelper = mockk(relaxed = true)
        csvTaskParser = mockk(relaxed = true)
        csvTask = CsvTask(csvFileHelper, CSV_FILE_NAME, csvTaskParser)
    }

    @Test
    fun `getAllTasks should returns list of tasks when csv file is not empty`(){
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTask.getAllTasks()

        // Then
        assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getAllTasks should returns empty list when csv file is empty`() {
        // Given
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertThat(csvTask.getAllTasks()).isEmpty()
    }


    @Test
    fun `getAllTasks should rethrows Exception when file throws Exception`(){
        // Given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.getAllTasks() }
    }

    @Test
    fun `getTaskByProjectId should returns empty list when csv file is empty`() {
        // Given
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        val result = csvTask.getTaskByProjectId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTaskByProjectId should returns empty list when csv file not contains any task with the same project id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }

        // When && Then
        val result = csvTask.getTaskByProjectId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTaskByProjectId should returns tasks list when csv file contains tasks with the same project id`() {
        // Given
        val projectId = UUID.randomUUID()
        val tasks = listOf(createTask(projectId = projectId), createTask(projectId = projectId), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When && Then
        val result = csvTask.getTaskByProjectId(projectId)
        assertThat(result).containsExactly(tasks[0], tasks[1])
    }

    @Test
    fun `getTaskByProjectId should rethrows Exception when file throws Exception`(){
        // Given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.getTaskByProjectId(UUID.fromString("1")) }
    }

    @Test
    fun `getTaskById should returns task when csv file contains a task with the same id`(){
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTask.getTaskById(task.taskId)

        // Then
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `getTaskById should returns null when csv file not contains a task with the same id`(){
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTask.getTaskById(task.taskId)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `getTaskById should rethrows Exception when file throws Exception`(){
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.getTaskById(task.taskId) }
    }

    @Test
    fun `createTask should returns when added new task successfully added to the csv file`(){
        // Given
        val newTask = createTask()

        // When
        csvTask.createTask(newTask)

        // Then
        verify(exactly = 1) { csvFileHelper.appendFile(any(), any()) }
    }

    @Test
    fun `createTask should rethrows Exception when file throws Exception`(){
        // Given
        val task = createTask()
        every { csvFileHelper.appendFile(any(), any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.createTask(task) }
    }

    @Test
    fun `editTask should returns when updating task successfully into the csv file`(){
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val newTask = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        csvTask.editTask(newTask)

        // Then
        verify(exactly = 1) { csvFileHelper.writeFile(any(), any()) }
    }

    @Test
    fun `editTask should rethrows Exception when file throws Exception`(){
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.editTask(task) }
    }

    @Test
    fun `deleteTask should returns when deleted task successfully from the csv file`(){
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        csvTask.deleteTask(task.taskId.toString())

        // Then
        verify(exactly = 1) { csvFileHelper.writeFile(any(), any()) }
    }

    @Test
    fun `deleteTask should rethrows Exception when file throws Exception`(){
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTask.deleteTask(task.taskId.toString()) }
    }

    companion object {
        private const val CSV_FILE_NAME = "build/tasks_test.csv"
    }
}