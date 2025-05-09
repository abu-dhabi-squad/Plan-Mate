package logic.task

import com.google.common.truth.Truth.assertThat
import helper.createTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.TaskNotFoundException
import logic.repository.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByIdUseCaseTest: GetTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk<TaskRepository>(relaxed = true)
        getTasksByIdUseCaseTest = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should get task by task id when task with same id exists`() = runTest{
        // Given
        val uuid= UUID.randomUUID()
        val taskId = uuid
        val task = createTask(id = taskId)
        coEvery { taskRepository.getTaskById(any()) } returns task

        // When
        val result = getTasksByIdUseCaseTest(taskId)

        // Then
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `should throw TaskNotFoundException when there is no task with the same id`() = runTest{
        // Given
        val taskId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { taskRepository.getTaskById(any()) } returns null

        // When && Then
        assertThrows<TaskNotFoundException> {
            getTasksByIdUseCaseTest(taskId)
        }
    }
}