package logic.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.task.GetTaskByIdUseCase

class GetTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByIdUseCaseTest: GetTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk<TaskRepository>(relaxed = true)
        getTasksByIdUseCaseTest = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should get task by task id when task with same id exists`() {
        // Given
        val taskId = "1"
        val task = createTask(id = taskId)
        every { taskRepository.getAllTasks() } returns listOf(task)

        // when
        val result = getTasksByIdUseCaseTest(taskId)

        // then
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `should throw TaskNotFoundException when there is no task with the same id`() {
        // given
        val taskId = "1"
        every { taskRepository.getAllTasks() } returns listOf(
            createTask(),
            createTask(),
            createTask()
        )

        // when && then
        assertThrows<TaskNotFoundException> {
            getTasksByIdUseCaseTest(taskId)
        }
    }
}