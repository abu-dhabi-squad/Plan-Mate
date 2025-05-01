package logic.task

import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.InvalidTaskDateException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.task.CreateTaskUseCase
import squad.abudhabi.logic.validation.TaskValidator
import java.time.LocalDate

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
    fun `should create a task when valid data is provided`() {
        // Given
        val task = Task(
            id = "11111",
            userName = "11111",
            projectId = "11111",
            stateId = "11111",
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
    ) {
        // Given
        val task = createTask(
            startDate = LocalDate.parse("2025-05-01"),
            endDate = LocalDate.parse("2025-04-01"),
        )
        every { taskValidator.validateOrThrow(any()) } throws InvalidTaskDateException()

        // When && Then
        assertThrows<InvalidTaskDateException> { createTaskUseCase(task) }
    }

    @Test
    fun `should throw Exception when repository throws Exception`(
    ) {
        every { taskRepository.createTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { createTaskUseCase(createTask()) }
    }
}