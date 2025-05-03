package logic.task

import io.mockk.every
import io.mockk.mockk
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.InvalidTaskDateException
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import logic.validation.TaskValidator
import java.time.LocalDate

class EditTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskValidator: TaskValidator
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk<TaskRepository>(relaxed = true)
        taskValidator = mockk<TaskValidator>(relaxed = true)
        editTaskUseCase = EditTaskUseCase(taskRepository, taskValidator)
    }

    @Test
    fun `should update a task when valid data is provided`() {
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
        assertDoesNotThrow { editTaskUseCase(task) }
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
        assertThrows<InvalidTaskDateException> { editTaskUseCase(task) }
    }

    @Test
    fun `should throw TaskNotFoundException when task id not found in non empty list`(
    ) {
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
        every { taskRepository.getTaskById(any()) } returns null

        // When && Then
        assertThrows<TaskNotFoundException> { editTaskUseCase(task) }
    }

    @Test
    fun `should throw Exception when repository throws Exception`(
    ) {
        every { taskRepository.editTask(any()) } throws Exception()

        // When && Then
        assertThrows<Exception> { editTaskUseCase(createTask()) }
    }
}