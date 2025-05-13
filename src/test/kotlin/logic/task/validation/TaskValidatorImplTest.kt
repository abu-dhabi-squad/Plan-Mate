import logic.exceptions.InvalidTaskDateException
import logic.model.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import presentation.logic.task.validation.TaskValidatorImpl
import java.time.LocalDate
import java.util.UUID

class TaskValidatorImplTest {

    private val validator = TaskValidatorImpl()

    @Test
    fun `should throw InvalidTaskDateException when endDate is before startDate`() {
        // Given
        val task = Task(
            username = "user1",
            projectId = UUID.randomUUID(),
            taskStateId = UUID.randomUUID(),
            title = "Invalid Task",
            description = "End date before start date",
            startDate = LocalDate.of(2025, 5, 10),
            endDate = LocalDate.of(2025, 5, 9)
        )

        // When & Then
        assertThrows<InvalidTaskDateException> {
            validator.validateOrThrow(task)
        }
    }

    @Test
    fun `should pass validation when endDate is after startDate`() {
        // Given
        val task = Task(
            username = "user1",
            projectId = UUID.randomUUID(),
            taskStateId = UUID.randomUUID(),
            title = "Valid Task",
            description = "Valid date range",
            startDate = LocalDate.of(2025, 5, 10),
            endDate = LocalDate.of(2025, 5, 11)
        )

        // When & Then
        assertDoesNotThrow {
            validator.validateOrThrow(task)
        }
    }

    @Test
    fun `should pass validation when endDate is equal to startDate`() {
        // Given
        val date = LocalDate.of(2025, 5, 10)
        val task = Task(
            username = "user1",
            projectId = UUID.randomUUID(),
            taskStateId = UUID.randomUUID(),
            title = "Same Date Task",
            description = "Start and end are the same",
            startDate = date,
            endDate = date
        )

        // When & Then
        assertDoesNotThrow {
            validator.validateOrThrow(task)
        }
    }
}