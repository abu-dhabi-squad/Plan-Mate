package data.task.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import squad.abudhabi.data.task.datasource.CsvTaskDataSource
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task

class CsvTaskDataSourceTest {
    private lateinit var csvFile: File
    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var csvTaskDataSource: CsvTaskDataSource

    @BeforeEach
    fun setup() {
        File(CSV_FILE_NAME).delete()
        csvFile = File(CSV_FILE_NAME)
        csvFileHelper = mockk(relaxed = true)
        csvTaskDataSource = CsvTaskDataSource(csvFileHelper, csvFile)
    }

    @Test
    fun `getAllTasks should returns list of tasks when csv file is not empty`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }

        // When
        val result = csvTaskDataSource.getAllTasks()

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getAllTasks should returns empty list when csv file is empty or not found`() {
        // Given
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertTrue { csvTaskDataSource.getAllTasks().isEmpty() }
    }

    @Test
    fun `getAllTasks should rethrows Exception when file throws Exception`() {
        // Given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.getAllTasks() }
    }

    @Test
    fun `getTaskById should returns task when csv file contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }

        // When
        val result = csvTaskDataSource.getTaskById(task.id)

        // Then
        Truth.assertThat(result).isEqualTo(task)
    }

    @Test
    fun `getTaskById should throws TaskNotFoundException when csv file not contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.getTaskById(task.id) }
    }

    @Test
    fun `getTaskById should throws TaskNotFoundException when csv file is empty or not found`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.getTaskById(task.id) }
    }

    @Test
    fun `getTaskById should rethrows Exception when file throws Exception`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.getTaskById(task.id) }
    }

    @Test
    fun `createTask should returns true when added new task successfully added to the csv file`() {
        // Given
        val newTask = createTask()
        every { csvFileHelper.appendFile(any(), any()) } returns true

        // When && Then
        assertTrue { csvTaskDataSource.createTask(newTask) }
    }

    @Test
    fun `createTask should returns false when couldn't add new task to the csv file`() {
        // Given
        val newTask = createTask()
        every { csvFileHelper.appendFile(any(), any()) } returns false

        // When && Then
        assertFalse { csvTaskDataSource.createTask(newTask) }
    }

    @Test
    fun `createTask should rethrows Exception when file throws Exception`() {
        // Given
        val task = createTask()
        every { csvFileHelper.appendFile(any(), any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.createTask(task) }
    }

    @Test
    fun `editTask should returns true when updating task successfully into the csv file`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val newTask = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }
        every { csvFileHelper.writeFile(any(), any()) } returns true

        // When && Then
        assertTrue { csvTaskDataSource.editTask(newTask) }
    }

    @Test
    fun `editTask should throws TaskNotFoundException when csv file not contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val newTask = createTask()
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.editTask(newTask) }
    }

    @Test
    fun `editTask should throws TaskNotFoundException when csv file is empty or not found`() {
        // Given
        val newTask = createTask()
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.editTask(newTask) }
    }

    @Test
    fun `editTask should rethrows Exception when file throws Exception`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.editTask(task) }
    }

    @Test
    fun `deleteTask should returns true when deleted task successfully from the csv file`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }
        every { csvFileHelper.writeFile(any(), any()) } returns true

        // When && Then
        assertTrue { csvTaskDataSource.deleteTask(task.id) }
    }

    @Test
    fun `deleteTask should throws TaskNotFoundException when csv file not contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns tasks.map { it.toCsvLine() }

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.deleteTask(task.id) }
    }

    @Test
    fun `deleteTask should throws TaskNotFoundException when csv file is empty or not found`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertThrows<TaskNotFoundException> { csvTaskDataSource.deleteTask(task.id) }
    }

    @Test
    fun `deleteTask should rethrows Exception when file throws Exception`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.deleteTask(task.id) }
    }

    private fun Task.toCsvLine() =
        "${id},${userName},${projectId},${stateId},${title},${description},${startDate},${endDate}"

    companion object {
        private const val CSV_FILE_NAME = "build/tasks_test.csv"
    }
}