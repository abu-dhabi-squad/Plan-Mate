package data.task.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.data.task.repository.TaskRepositoryImpl
import squad.abudhabi.logic.exceptions.TaskNotFoundException
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
    fun `getTaskById should rethrows TaskNotFoundException when datasource not contains a task with the same id`() {
        // Given
        val task = createTask()
        every { taskDataSource.getTaskById(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.getTaskById(task.id) }
    }

    @Test
    fun `getTaskById should rethrows TaskNotFoundException when datasource is empty`() {
        // Given
        val task = createTask()
        every { taskDataSource.getTaskById(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.getTaskById(task.id) }
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
    fun `createTask should returns true when added new task successfully added to the datasource`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.createTask(any()) } returns true

        // When && Then
        assertTrue { taskRepository.createTask(newTask) }
    }

    @Test
    fun `createTask should returns false when couldn't add new task to the datasource`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.createTask(any()) } returns false

        // When && Then
        assertFalse { taskRepository.createTask(newTask) }
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
    fun `editTask should returns true when updating task successfully into the datasource`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.editTask(any()) } returns true

        // When && Then
        assertTrue { taskRepository.editTask(newTask) }
    }

    @Test
    fun `editTask should rethrows TaskNotFoundException when datasource not contains a task with the same id`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.editTask(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.editTask(newTask) }
    }

    @Test
    fun `editTask should rethrows TaskNotFoundException when datasource is empty`() {
        // Given
        val newTask = createTask()
        every { taskDataSource.editTask(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.editTask(newTask) }
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
        every { taskDataSource.deleteTask(any()) } returns true

        // When && Then
        assertTrue { taskRepository.deleteTask(task.id) }
    }

    @Test
    fun `deleteTask should rethrows TaskNotFoundException when datasource not contains a task with the same id`() {
        // Given
        val task = createTask()
        every { taskDataSource.deleteTask(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.deleteTask(task.id) }
    }

    @Test
    fun `deleteTask should rethrows TaskNotFoundException when datasource is empty`() {
        // Given
        val task = createTask()
        every { taskDataSource.deleteTask(any()) } throws TaskNotFoundException()

        // When && Then
        assertThrows<TaskNotFoundException> { taskRepository.deleteTask(task.id) }
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