package logic.validation

import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import logic.exceptions.InvalidTaskDateException
import java.time.LocalDate

class TaskValidatorImplTest {
    private lateinit var taskValidator: TaskValidatorImpl
    @BeforeEach
    fun setup() {
        taskValidator = TaskValidatorImpl()
    }

    @Test
    fun `should not throw exception when task data is valid`() {
        // Given
        val task = createTask(
            startDate = LocalDate.parse("2025-04-01"),
            endDate = LocalDate.parse("2025-05-01"),
        )

        // When && Then
        assertDoesNotThrow { taskValidator.validateOrThrow(task) }
    }

    @Test
    fun `should throw InvalidTaskDateException when end date is before start date`(){
        // Given
        val task = createTask(
            startDate = LocalDate.parse("2025-05-01"),
            endDate = LocalDate.parse("2025-04-01"),
        )

        // When && Then
        assertThrows<InvalidTaskDateException> { taskValidator.validateOrThrow(task) }
    }
}