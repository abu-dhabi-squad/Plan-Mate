import logic.exceptions.InvalidTaskDateException
import logic.model.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import presentation.logic.task.validation.TaskValidatorImpl
import java.time.LocalDate
import java.util.*

class TaskValidatorImplTest {

    private val validator = TaskValidatorImpl()

    @Test
    fun `should throw InvalidTaskDateException when endDate is before startDate`() {
        // given
        val task = Task(
            username = "user1",
            projectId = UUID.randomUUID(),
            taskStateId = UUID.randomUUID(),
            title = "Invalid Task",
            description = "End date before start date",
            startDate = LocalDate.of(2025, 5, 10),
            endDate = LocalDate.of(2025, 5, 9)
        )

        // when & then
        assertThrows<InvalidTaskDateException> {
            validator.validateOrThrow(task)
        }
    }

    @Test
    fun `should pass validation when endDate is after startDate`() {
        // given
        val task = Task(
            username = "user1",
            projectId = UUID.randomUUID(),
            taskStateId = UUID.randomUUID(),
            title = "Valid Task",
            description = "Valid date range",
            startDate = LocalDate.of(2025, 5, 10),
            endDate = LocalDate.of(2025, 5, 11)
        )

        // when & then
        assertDoesNotThrow {
            validator.validateOrThrow(task)
        }
    }

    @Test
    fun `should pass validation when endDate is equal to startDate`() {
        // given
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

        // when & then
        assertDoesNotThrow {
            validator.validateOrThrow(task)
        }
    }
}