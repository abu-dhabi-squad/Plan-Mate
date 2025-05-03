package logic.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.repository.TaskRepository
import java.util.UUID

class DeleteTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        deleteTaskByIdUseCase = DeleteTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should delete task by its id when task is exists`() {
        // Given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getTaskById(any()) } returns createTask(id = taskId)

        // When
        deleteTaskByIdUseCase(taskId)

        // Then
        verify { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `should throw TaskNotFoundException exception when task is not exists`() {
        // Given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getTaskById(any()) } returns null

        // When && Then
        assertThrows<TaskNotFoundException> {
            deleteTaskByIdUseCase(taskId)
        }
    }
}