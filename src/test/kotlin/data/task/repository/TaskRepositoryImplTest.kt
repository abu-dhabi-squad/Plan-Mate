package data.task.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.helper.createTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue
import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.data.task.repository.TaskRepositoryImpl
import squad.abudhabi.logic.repository.TaskRepository

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository

    @BeforeEach
    fun setup() {
        taskDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `getAllTasks should returns list of tasks when datasource is not empty`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { taskDataSource.getAllTasks() } returns tasks

        // When
        val result = taskRepository.getAllTasks()

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getAllTasks should returns empty list when datasource is empty`() {
        // Given
        every { taskDataSource.getAllTasks() } returns emptyList()

        // When && Then
        assertTrue { taskRepository.getAllTasks().isEmpty() }
    }

    @Test
    fun `getAllTasks should rethrows Exception when datasource throws Exception`() {
        // Given
        every { taskDataSource.getAllTasks() } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getAllTasks() }
    }

    @Test
    fun `getTaskByProjectId should returns list of tasks when datasource is not empty`() {
        // Given
        val tasks = listOf(createTask(), createTask(), createTask(), createTask())
        every { taskDataSource.getTaskByProjectId(any()) } returns tasks

        // When
        val result = taskRepository.getTaskByProjectId(tasks[0].projectId)

        // Then
        Truth.assertThat(result).containsExactly(*tasks.toTypedArray())
    }

    @Test
    fun `getTaskByProjectId should returns empty list when datasource is empty`() {
        // Given
        every { taskDataSource.getTaskByProjectId(any()) } returns emptyList()

        // When && Then
        assertTrue { taskRepository.getTaskByProjectId("1").isEmpty() }
    }

    @Test
    fun `getTaskByProjectId should rethrows Exception when datasource throws Exception`() {
        // Given
        every { taskDataSource.getTaskByProjectId(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getTaskByProjectId("1") }
    }

    @Test
    fun `getTaskById should returns task when datasource contains a task with the same id`() {
        // Given
        val task = createTask()
        every { taskDataSource.getTaskById(any()) } returns task

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        Truth.assertThat(result).isEqualTo(task)
    }

    @Test
    fun `getTaskById should returns null when datasource not contains a task with the same id`() {
        // Given
        val task = createTask()
        every { taskDataSource.getTaskById(any()) } returns null

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        Truth.assertThat(result).isNull()
    }

    @Test
    fun `getTaskById should rethrows Exception when datasource throws Exception`() {
        // Given
        val task = createTask()
        every { taskDataSource.getTaskById(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.getTaskById(task.id) }
    }

    @Test
    fun `createTask should returns when added new task successfully added to the datasource`() {
        // Given
        val newTask = createTask()

        // When
        taskRepository.createTask(newTask)

        // Then
        verify(exactly = 1) { taskDataSource.createTask(any()) }
    }

    @Test
    fun `createTask should returns false when couldn't add new task to the datasource`() {
        // Given
        val newTask = createTask()

        // When
        taskRepository.createTask(newTask)

        // Then
        verify(exactly = 1) { taskDataSource.createTask(any()) }
    }

    @Test
    fun `createTask should rethrows Exception when datasource throws Exception`() {
        // Given
        val task = createTask()
        every { taskDataSource.createTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.createTask(task) }
    }

    @Test
    fun `editTask should returns when updating task successfully into the datasource`() {
        // Given
        val newTask = createTask()

        // When
        taskRepository.editTask(newTask)

        // Then
        verify(exactly = 1) { taskDataSource.editTask(any()) }
    }

    @Test
    fun `editTask should rethrows Exception when datasource throws Exception`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.editTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.editTask(newTask) }
    }

    @Test
    fun `deleteTask should returns true when deleted task successfully from the datasource`() {
        // Given
        val task = createTask()

        // When
        taskRepository.deleteTask(task.id)

        // Then
        verify(exactly = 1) { taskDataSource.deleteTask(any()) }
    }

    @Test
    fun `deleteTask should rethrows Exception when datasource throws Exception`() {
        // Given
        val task = createTask()
        every { taskDataSource.deleteTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { taskRepository.deleteTask(task.id) }
    }
}