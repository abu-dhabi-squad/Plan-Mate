package data.task.repository

import com.google.common.truth.Truth
import helper.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue
import data.task.datasource.csv_datasource.LocalTaskDataSource
import logic.repository.TaskRepository

class TaskRepositoryImplTest {
    private lateinit var localTaskDataSource: LocalTaskDataSource
    private lateinit var taskRepository: TaskRepository

    @BeforeEach
    fun setup() {
        localTaskDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(localTaskDataSource)
    }

    @Test
    fun `getAllTasks should returns list of tasks when datasource is not empty`() = runTest{
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        coEvery { localTaskDataSource.getAllTasks() } returns tasks

        // When
        val result = taskRepository.getAllTasks()

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getAllTasks should returns empty list when datasource is empty`()= runTest {
        // Given
        coEvery { localTaskDataSource.getAllTasks() } returns emptyList()

        // When && Then
        assertTrue { taskRepository.getAllTasks().isEmpty() }
    }

    @Test
    fun `getAllTasks should rethrows Exception when datasource throws Exception`() = runTest{
        // Given
        coEvery { localTaskDataSource.getAllTasks() } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getAllTasks() }
    }

    @Test
    fun `getTaskByProjectId should returns list of tasks when datasource is not empty`()= runTest {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        coEvery { localTaskDataSource.getTaskByProjectId(any()) } returns tasks

        // When
        val result = taskRepository.getTaskByProjectId(tasks[0].projectId)

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getTaskByProjectId should returns empty list when datasource is empty`() = runTest{
        // Given
        coEvery { localTaskDataSource.getTaskByProjectId(any()) } returns emptyList()

        // When && Then
        assertTrue { taskRepository.getTaskByProjectId("1").isEmpty() }
    }

    @Test
    fun `getTaskByProjectId should rethrows Exception when datasource throws Exception`() = runTest{
        // Given
        coEvery { localTaskDataSource.getTaskByProjectId(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getTaskByProjectId("1") }
    }

    @Test
    fun `getTaskById should returns task when datasource contains a task with the same id`() = runTest{
        // Given
        val task = createTask()
        coEvery { localTaskDataSource.getTaskById(any()) } returns task

        // When
        val result = taskRepository.getTaskById(task.id.toString())

        // Then
        Truth.assertThat(result).isEqualTo(task)
    }

    @Test
    fun `getTaskById should returns null when datasource not contains a task with the same id`()= runTest {
        // Given
        val task = createTask()
        coEvery { localTaskDataSource.getTaskById(any()) } returns null

        // When
        val result = taskRepository.getTaskById(task.id.toString())

        // Then
        Truth.assertThat(result).isNull()
    }

    @Test
    fun `getTaskById should rethrows Exception when datasource throws Exception`() = runTest{
        // Given
        val task = createTask()
        coEvery { localTaskDataSource.getTaskById(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getTaskById(task.id.toString()) }
    }

    @Test
    fun `createTask should returns when added new task successfully added to the datasource`() = runTest{
        // Given
        val newTask = createTask()

        // When
        taskRepository.createTask(newTask)

        // Then
        coVerify(exactly = 1) { localTaskDataSource.createTask(any()) }
    }

    @Test
    fun `createTask should returns false when couldn't add new task to the datasource`() = runTest{
        // Given
        val newTask = createTask()

        // When
        taskRepository.createTask(newTask)

        // Then
        coVerify(exactly = 1) { localTaskDataSource.createTask(any()) }
    }

    @Test
    fun `createTask should rethrows Exception when datasource throws Exception`()= runTest {
        // Given
        val task = createTask()
        coEvery { localTaskDataSource.createTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.createTask(task) }
    }

    @Test
    fun `editTask should returns when updating task successfully into the datasource`() = runTest{
        // Given
        val newTask = createTask()

        // When
        taskRepository.editTask(newTask)

        // Then
        coVerify(exactly = 1) { localTaskDataSource.editTask(any()) }
    }

    @Test
    fun `editTask should rethrows Exception when datasource throws Exception`()= runTest {
        // Given
        val newTask = createTask()
        coEvery { localTaskDataSource.editTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.editTask(newTask) }
    }

    @Test
    fun `deleteTask should returns true when deleted task successfully from the datasource`() = runTest{
        // Given
        val task = createTask()

        // When
        taskRepository.deleteTask(task.id.toString())

        // Then
        coVerify(exactly = 1) { localTaskDataSource.deleteTask(any()) }
    }

    @Test
    fun `deleteTask should rethrows Exception when datasource throws Exception`() = runTest{
        // Given
        val task = createTask()
        coEvery { localTaskDataSource.deleteTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.deleteTask(task.id.toString()) }
    }
}