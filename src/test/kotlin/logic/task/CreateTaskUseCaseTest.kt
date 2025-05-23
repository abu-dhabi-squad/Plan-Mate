package logic.task

import helper.createTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import logic.exceptions.InvalidTaskDateException
import logic.model.Task
import logic.repository.TaskRepository
import logic.task.validation.TaskValidator
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CreateTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskValidator: TaskValidator
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk<TaskRepository>(relaxed = true)
        taskValidator = mockk<TaskValidator>(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(taskRepository, taskValidator)
    }

    @Test
    fun `should create a task when valid data is provided`()= runTest {
        // Given
        val task = Task(
            username = "11111",
            projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"),
            taskStateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-02-01"),
        )

        // When & Then
        assertDoesNotThrow { createTaskUseCase(task) }
    }

    @Test
    fun `should throw InvalidTaskDateException when task start data is before end date`(
    ) = runTest{
        // Given
        val task = createTask(
            startDate = LocalDate.parse("2025-05-01"),
            endDate = LocalDate.parse("2025-04-01"),
        )
        coEvery { taskValidator.validateOrThrow(any()) } throws InvalidTaskDateException()

        // When && Then
        assertThrows<InvalidTaskDateException> { createTaskUseCase(task) }
    }

    @Test
    fun `should throw Exception when repository throws Exception`(
    )= runTest {
        coEvery { taskRepository.createTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { createTaskUseCase(createTask()) }
    }
}