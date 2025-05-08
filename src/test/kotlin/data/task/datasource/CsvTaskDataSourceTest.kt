package data.task.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.helper.createTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue
import data.task.parser.CsvTaskParser
import data.utils.filehelper.CsvFileHelper

class CsvTaskDataSourceTest {
    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var csvTaskParser: CsvTaskParser
    private lateinit var csvTaskDataSource: CsvTaskDataSource

    @BeforeEach
    fun setup() {
        csvFileHelper = mockk(relaxed = true)
        csvTaskParser = mockk(relaxed = true)
        csvTaskDataSource = CsvTaskDataSource(csvFileHelper, CSV_FILE_NAME, csvTaskParser)
    }

    @Test
    fun `getAllTasks should returns list of tasks when csv file is not empty`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTaskDataSource.getAllTasks()

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getAllTasks should returns empty list when csv file is empty`() {
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
    fun `getTaskByProjectId should returns list of tasks when csv file is not empty`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTaskDataSource.getTaskByProjectId(tasks[0].projectId)

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getTaskByProjectId should returns empty list when csv file is empty`() {
        // Given
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // When && Then
        assertTrue { csvTaskDataSource.getTaskByProjectId("1").isEmpty() }
    }

    @Test
    fun `getTaskByProjectId should rethrows Exception when file throws Exception`() {
        // Given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.getTaskByProjectId("1") }
    }

    @Test
    fun `getTaskById should returns task when csv file contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTaskDataSource.getTaskById(task.id)

        // Then
        Truth.assertThat(result).isEqualTo(task)
    }

    @Test
    fun `getTaskById should returns null when csv file not contains a task with the same id`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = createTask()
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        val result = csvTaskDataSource.getTaskById(task.id)

        // Then
        Truth.assertThat(result).isNull()
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
    fun `createTask should returns when added new task successfully added to the csv file`() {
        // Given
        val newTask = createTask()

        // When
        csvTaskDataSource.createTask(newTask)

        // Then
        verify(exactly = 1) { csvFileHelper.appendFile(any(), any()) }
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
    fun `editTask should returns when updating task successfully into the csv file`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val newTask = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        csvTaskDataSource.editTask(newTask)

        // Then
        verify(exactly = 1) { csvFileHelper.writeFile(any(), any()) }
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
    fun `deleteTask should returns when deleted task successfully from the csv file`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        val task = tasks[0]
        every { csvFileHelper.readFile(any()) } returns tasks.map { csvTaskParser.getCsvLineFromTask(it) }
        every { csvTaskParser.getTaskFromCsvLine(any()) } returnsMany tasks

        // When
        csvTaskDataSource.deleteTask(task.id)

        // Then
        verify(exactly = 1) { csvFileHelper.writeFile(any(), any()) }
    }

    @Test
    fun `deleteTask should rethrows Exception when file throws Exception`() {
        // Given
        val task = createTask()
        every { csvFileHelper.readFile(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { csvTaskDataSource.deleteTask(task.id) }
    }

    companion object {
        private const val CSV_FILE_NAME = "build/tasks_test.csv"
    }
}