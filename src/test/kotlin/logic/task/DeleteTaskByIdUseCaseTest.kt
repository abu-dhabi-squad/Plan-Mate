package logic.task

import helper.createTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.TaskNotFoundException
import logic.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        deleteTaskByIdUseCase = DeleteTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should delete task by its id when task is exists`() = runTest{
        // Given
        val taskId = Uuid.random()
        coEvery { taskRepository.getTaskById(any()) } returns createTask(id = taskId)

        // When
        deleteTaskByIdUseCase(taskId)

        // Then
        coVerify { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `should throw TaskNotFoundException exception when task is not exists`() = runTest{
        // Given
        val taskId = Uuid.random()
        coEvery { taskRepository.getTaskById(any()) } returns null

        // When && Then
        assertThrows<TaskNotFoundException> {
            deleteTaskByIdUseCase(taskId)
        }
    }
}