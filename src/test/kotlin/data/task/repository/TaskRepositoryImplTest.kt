package data.task.repository

import com.google.common.truth.Truth.assertThat
import helper.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import data.task.mapper.MongoTaskMapper
import data.task.model.TaskDto
import logic.repository.TaskRepository
import java.util.*

class TaskRepositoryImplTest {

    private lateinit var remoteTaskDataSource: RemoteTaskDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskMapper: MongoTaskMapper

    @BeforeEach
    fun setup() {
        remoteTaskDataSource = mockk(relaxed = true)
        taskMapper = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(remoteTaskDataSource, taskMapper)
    }

    @Test
    fun `getAllTasks should return list of tasks when datasource is not empty`() = runTest {
        val taskDto1 = mockk<TaskDto>()
        val taskDto2 = mockk<TaskDto>()
        val task1 = createTask()
        val task2 = createTask()

        coEvery { remoteTaskDataSource.getAllTasks() } returns listOf(taskDto1, taskDto2)
        every { taskMapper.taskDtoToTask(taskDto1) } returns task1
        every { taskMapper.taskDtoToTask(taskDto2) } returns task2

        val result = taskRepository.getAllTasks()

        assertThat(result).containsExactly(task1, task2)
        verify(exactly = 1) { taskMapper.taskDtoToTask(taskDto1) }
        verify(exactly = 1) { taskMapper.taskDtoToTask(taskDto2) }
    }

    @Test
    fun `getAllTasks should return empty list when datasource is empty`() = runTest {
        coEvery { remoteTaskDataSource.getAllTasks() } returns emptyList()

        val result = taskRepository.getAllTasks()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllTasks should rethrow Exception when datasource throws Exception`() = runTest {
        coEvery { remoteTaskDataSource.getAllTasks() } throws Exception()

        assertThrows<Exception> { taskRepository.getAllTasks() }
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when datasource is not empty`() = runTest {
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        val taskDto1 = mockk<TaskDto>()
        val taskDto2 = mockk<TaskDto>()
        val task1 = createTask().copy(projectId = projectId)
        val task2 = createTask().copy(projectId = projectId)

        coEvery { remoteTaskDataSource.getTaskByProjectId(projectId.toString()) } returns listOf(taskDto1, taskDto2)
        every { taskMapper.taskDtoToTask(taskDto1) } returns task1
        every { taskMapper.taskDtoToTask(taskDto2) } returns task2

        val result = taskRepository.getTaskByProjectId(projectId)

        assertThat(result).containsExactly(task1, task2)
        verify(exactly = 1) { taskMapper.taskDtoToTask(taskDto1) }
        verify(exactly = 1) { taskMapper.taskDtoToTask(taskDto2) }
    }

    @Test
    fun `getTaskByProjectId should return empty list when datasource is empty`() = runTest {
        coEvery { remoteTaskDataSource.getTaskByProjectId(any()) } returns emptyList()

        val result = taskRepository.getTaskByProjectId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTaskByProjectId should rethrow Exception when datasource throws Exception`() = runTest {
        coEvery { remoteTaskDataSource.getTaskByProjectId(any()) } throws Exception()

        assertThrows<Exception> { taskRepository.getTaskByProjectId(UUID.fromString("1")) }
    }

    @Test
    fun `getTaskById should return task when datasource contains matching task`() = runTest {
        val task = createTask()
        val taskDto = mockk<TaskDto>()

        coEvery { remoteTaskDataSource.getTaskById(task.id.toString()) } returns taskDto
        every { taskMapper.taskDtoToTask(taskDto) } returns task

        val result = taskRepository.getTaskById(task.id)

        assertThat(result).isEqualTo(task)
        verify(exactly = 1) { taskMapper.taskDtoToTask(taskDto) }
    }

    @Test
    fun `getTaskById should return null when datasource returns null`() = runTest {
        val task = createTask()
        coEvery { remoteTaskDataSource.getTaskById(task.id.toString()) } returns null

        val result = taskRepository.getTaskById(task.id)

        assertThat(result).isNull()
    }

    @Test
    fun `getTaskById should rethrow Exception when datasource throws Exception`() = runTest {
        val task = createTask()
        coEvery { remoteTaskDataSource.getTaskById(task.id.toString()) } throws Exception()

        assertThrows<Exception> { taskRepository.getTaskById(task.id) }
    }

    @Test
    fun `createTask should call datasource with mapped dto`() = runTest {
        val task = createTask()
        val taskDto = mockk<TaskDto>()

        every { taskMapper.taskToTaskDto(task) } returns taskDto
        coEvery { remoteTaskDataSource.createTask(taskDto) } just Runs

        taskRepository.createTask(task)

        verify { taskMapper.taskToTaskDto(task) }
        coVerify { remoteTaskDataSource.createTask(taskDto) }
    }

    @Test
    fun `createTask should rethrow Exception when datasource throws Exception`() = runTest {
        val task = createTask()
        val taskDto = mockk<TaskDto>()

        every { taskMapper.taskToTaskDto(task) } returns taskDto
        coEvery { remoteTaskDataSource.createTask(taskDto) } throws Exception()

        assertThrows<Exception> { taskRepository.createTask(task) }
    }

    @Test
    fun `editTask should call datasource with mapped dto`() = runTest {
        val task = createTask()
        val taskDto = mockk<TaskDto>()

        every { taskMapper.taskToTaskDto(task) } returns taskDto
        coEvery { remoteTaskDataSource.editTask(taskDto) } just Runs

        taskRepository.editTask(task)

        verify { taskMapper.taskToTaskDto(task) }
        coVerify { remoteTaskDataSource.editTask(taskDto) }
    }

    @Test
    fun `editTask should rethrow Exception when datasource throws Exception`() = runTest {
        val task = createTask()
        val taskDto = mockk<TaskDto>()

        every { taskMapper.taskToTaskDto(task) } returns taskDto
        coEvery { remoteTaskDataSource.editTask(taskDto) } throws Exception()

        assertThrows<Exception> { taskRepository.editTask(task) }
    }

    @Test
    fun `deleteTask should call datasource with correct id`() = runTest {
        val task = createTask()

        coEvery { remoteTaskDataSource.deleteTask(task.id.toString()) } just Runs

        taskRepository.deleteTask(task.id)

        coVerify { remoteTaskDataSource.deleteTask(task.id.toString()) }
    }

    @Test
    fun `deleteTask should rethrow Exception when datasource throws Exception`() = runTest {
        val task = createTask()

        coEvery { remoteTaskDataSource.deleteTask(task.id.toString()) } throws Exception()

        assertThrows<Exception> { taskRepository.deleteTask(task.id) }
    }
}