import logic.exceptions.InvalidTaskDateException
import logic.model.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import logic.task.validation.TaskValidatorImpl
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskValidatorImplTest {

    private val validator = TaskValidatorImpl()

    @Test
    fun `should throw InvalidTaskDateException when endDate is before startDate`() {
        // Given
        val task = Task(
            username = "user1",
            projectId = Uuid.random(),
            taskStateId = Uuid.random(),
            title = "Invalid Task",
            description = "End date before start date",
            startDate = LocalDate(2025, 5, 10),
            endDate = LocalDate(2025, 5, 9)
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
            projectId = Uuid.random(),
            taskStateId = Uuid.random(),
            title = "Valid Task",
            description = "Valid date range",
            startDate = LocalDate(2025, 5, 10),
            endDate = LocalDate(2025, 5, 11)
        )

        // When & Then
        assertDoesNotThrow {
            validator.validateOrThrow(task)
        }
    }

    @Test
    fun `should pass validation when endDate is equal to startDate`() {
        // Given
        val date = LocalDate(2025, 5, 10)
        val task = Task(
            username = "user1",
            projectId = Uuid.random(),
            taskStateId = Uuid.random(),
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