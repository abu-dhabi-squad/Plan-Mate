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
        // given
        val projectId = UUID.randomUUID().toString()
        val firstRelatedTask = createTask(projectId = projectId)
        val secondRelatedTask = createTask(projectId = projectId)
        val nonRelatedTask = createTask()
        every { taskRepository.getAllTasks() } returns listOf(
            firstRelatedTask,
            secondRelatedTask,
            nonRelatedTask
        )
        // when
        val result = getTasksByProjectIdUseCase(projectId)
        // then
        assertThat(result).containsExactly(firstRelatedTask, secondRelatedTask)
    }

    @Test
    fun `should throw NoTasksFoundException when there are no tasks for provided project id`() {
        // given
        val projectId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns listOf(
            createTask(),
            createTask(),
            createTask()
        )
        // when && then
        assertThrows<NoTasksFoundException> {
            getTasksByProjectIdUseCase(projectId)
        }

    }
}