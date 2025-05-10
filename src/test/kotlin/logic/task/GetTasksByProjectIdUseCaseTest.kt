package logic.task

import com.google.common.truth.Truth.assertThat
import helper.createTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.NoTasksFoundException
import logic.repository.TaskRepository
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
    fun `should get tasks by project id when project is exists`()= runTest {
        // Given
        val projectId = UUID.randomUUID()
        val firstRelatedTask = createTask(projectId = projectId)
        val secondRelatedTask = createTask(projectId = projectId)
        coEvery { taskRepository.getTaskByProjectId(projectId) } returns listOf(
            firstRelatedTask,
            secondRelatedTask,
        )

        // When
        val result = getTasksByProjectIdUseCase(projectId)

        // Then
        assertThat(result).containsExactly(firstRelatedTask, secondRelatedTask)
    }

    @Test
    fun `should throw NoTasksFoundException when there are no tasks for provided project id`()= runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { taskRepository.getAllTasks() } returns listOf(
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