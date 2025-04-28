package logic.task

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.exceptions.IllegalTaskException
import squad.abudhabi.logic.exceptions.InvalidDateException
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.task.CreateTaskUseCase
import squad.abudhabi.logic.validation.TaskValidator

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
        // When
        val result = createTaskUseCase(
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = "2025-04-01",
            endDate = "2025-05-01",
        )

        // Then
        Truth.assertThat(result).isTrue()
        verify(exactly = 1) { taskRepository.createTask(any()) }
    }

    @ParameterizedTest
    @CsvSource(
        "'', ''",
        "2025-05-01, ''",
        "'', 2025-05-01",
    )
    fun `should throw InvalidDateException when task data is invalid`(
        startDate: String,
        endDate: String,
    ) {
        // given
        every { taskValidator.validateOrThrow(startDate, endDate) } throws InvalidDateException()

        // when && then
        assertThrows<InvalidDateException> {
            createTaskUseCase(
                userId = "11111",
                projectId = "11111",
                stateId = "11111",
                title = "Title",
                description = "Description",
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    @Test
    fun `should throw IllegalTaskException when task start data is before end date`(
    ) {
        // given
        val startDate = "2025-05-01"
        val endDate = "2025-04-01"

        every { taskValidator.validateOrThrow(startDate, endDate) } throws IllegalTaskException()
        // when && then
        assertThrows<IllegalTaskException> {
            createTaskUseCase(
                userId = "11111",
                projectId = "11111",
                stateId = "11111",
                title = "Title",
                description = "Description",
                startDate = startDate,
                endDate = endDate,
            )
        }
    }
}