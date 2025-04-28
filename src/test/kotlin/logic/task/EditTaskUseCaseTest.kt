package logic.task

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.exceptions.IllegalTaskException
import squad.abudhabi.logic.exceptions.InvalidDateException
import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.task.EditTaskUseCase
import squad.abudhabi.logic.validation.TaskValidator
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

    @ParameterizedTest
    @CsvSource(
        "'1', '', '', '', ''",
        "'', '1', '', '', ''",
        "'', '', '1', '', ''",
        "'', '', '', '2025-01-15', ''",
        "'', '', '', '', '2025-02-15'",
        "'1', '1', '1', '2025-01-15', '2025-02-15'",
    )
    fun `should update a task when valid data is provided`(
        stateId: String,
        title: String,
        description: String,
        startDate: String,
        endDate: String,
    ) {
        // given
        val oldTask = Task(
            id = "11111",
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-02-01"),
        )
        val newTask = oldTask.copy(
            stateId = stateId.ifBlank { oldTask.stateId },
            title = title.ifBlank { oldTask.title },
            description = description.ifBlank { oldTask.description },
            startDate = if (startDate.isNotBlank()) LocalDate.parse(startDate) else oldTask.startDate,
            endDate = if (endDate.isNotBlank()) LocalDate.parse(endDate) else oldTask.endDate,
        )
        every { taskRepository.getAllTasks() } returns listOf(oldTask)

        // When
        val result = editTaskUseCase(
            taskId = oldTask.id,
            stateId = stateId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
        )

        // Then
        Truth.assertThat(result).isTrue()
        verify(exactly = 1) { taskRepository.editTask(oldTask.id, newTask) }
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
        val task = Task(
            id = "11111",
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-08-01"),
        )
        every { taskValidator.validateOrThrow(startDate, endDate) } throws InvalidDateException()

        // when && then
        assertThrows<InvalidDateException> {
            editTaskUseCase(
                taskId = task.id,
                stateId = task.stateId,
                title = task.title,
                description = task.description,
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
        val task = Task(
            id = "11111",
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-08-01"),
        )
        every { taskValidator.validateOrThrow(startDate, endDate) } throws IllegalTaskException()

        // when && then
        assertThrows<IllegalTaskException> {
            editTaskUseCase(
                taskId = task.id,
                stateId = task.stateId,
                title = task.title,
                description = task.description,
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    @Test
    fun `should throw TaskNotFoundException when task id not found in empty list`(
    ) {
        // given
        val startDate = "2025-05-01"
        val endDate = "2025-04-01"
        val task = Task(
            id = "11111",
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-08-01"),
        )
        every { taskRepository.getAllTasks() } returns listOf()

        // when && then
        assertThrows<TaskNotFoundException> {
            editTaskUseCase(
                taskId = task.id,
                stateId = task.stateId,
                title = task.title,
                description = task.description,
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    @Test
    fun `should throw TaskNotFoundException when task id not found in non empty list`(
    ) {
        // given
        val startDate = "2025-05-01"
        val endDate = "2025-04-01"
        val task = Task(
            id = "11111",
            userId = "11111",
            projectId = "11111",
            stateId = "11111",
            title = "Title",
            description = "Description",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-08-01"),
        )
        every { taskRepository.getAllTasks() } returns listOf(createTask())

        // when && then
        assertThrows<TaskNotFoundException> {
            editTaskUseCase(
                taskId = task.id,
                stateId = task.stateId,
                title = task.title,
                description = task.description,
                startDate = startDate,
                endDate = endDate,
            )
        }
    }
}