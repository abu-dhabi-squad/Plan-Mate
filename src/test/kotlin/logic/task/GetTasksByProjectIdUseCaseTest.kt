package logic.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.task.GetTasksByProjectIdUseCase
import java.util.UUID

class GetTasksByProjectIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk<TaskRepository>(relaxed = true)
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }

    @Test
    fun `should get tasks by project id when project is exists`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val firstRelatedTask = createTask(projectId = projectId)
        val secondRelatedTask = createTask(projectId = projectId)
        every { taskRepository.getTaskByProjectId(projectId) } returns listOf(
            firstRelatedTask,
            secondRelatedTask,
        )

        // When
        val result = getTasksByProjectIdUseCase(projectId)

        // Then
        assertThat(result).containsExactly(firstRelatedTask, secondRelatedTask)
    }

    @Test
    fun `should throw NoTasksFoundException when there are no tasks for provided project id`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns listOf(
            createTask(),
            createTask(),
            createTask()
        )

        // When && Then
        assertThrows<NoTasksFoundException> {
            getTasksByProjectIdUseCase(projectId)
        }

    }
}