package presentation.utils

import com.google.common.truth.Truth
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.io.InputReader
import presentation.io.Printer
import logic.utils.DateParser
import java.time.LocalDate
import kotlin.test.Test

class PromptServiceTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: InputReader = mockk(relaxed = true)
    private val dateParser: DateParser = mockk(relaxed = true)
    private lateinit var promptService: PromptService

    @BeforeEach
    fun setup() {
        promptService = PromptService(
            printer = printer,
            reader = reader,
            dateParser = dateParser,
        )
    }

    @Test
    fun `promptNonEmptyInt should should return int`() = runTest {
        // Given
        val expectedRes = 1
        every { reader.readInt() } returns null andThen expectedRes
        // When
        val res = promptService.promptNonEmptyInt("inter integer")
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
        verify(exactly = 1) { printer.displayLn(match { it.toString().contains("cannot be empty") }) }
    }

    @Test
    fun `promptNonEmptyString should should return String`() = runTest {
        // Given
        val expectedRes = "name"
        every { reader.readString() } returns null andThen expectedRes
        // When
        val res = promptService.promptNonEmptyString("inter String")
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
        verify(exactly = 1) { printer.displayLn(match { it.toString().contains("cannot be empty") }) }
    }

    @Test
    fun `promptString should should return new String when entered`() = runTest {
        // Given
        val expectedRes = "name"
        every { reader.readString() } returns expectedRes
        // When
        val res = promptService.promptString("inter String", "")
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
    }

    @Test
    fun `promptDate should should return date when entering date`() = runTest {
        // Given
        val expectedRes = LocalDate.of(2002, 8, 10)
        every { reader.readString() } returns null andThen "2002-8-10"
        every { dateParser.parseDateFromString(any()) } returns expectedRes
        // When
        val res = promptService.promptDate("inter date")
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
        verify(exactly = 1) { printer.displayLn(match { it.toString().contains("cannot be empty") }) }
    }

    @Test
    fun `promptDate should should display error message when parser throw exception`() = runTest {
        // Given
        val expectedRes = LocalDate.of(2002, 8, 10)
        every { reader.readString() } returns "2002-8-10"
        every { dateParser.parseDateFromString(any()) } throws Exception() andThen expectedRes
        // When
        val res = promptService.promptDate("inter date")
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
        verify(exactly = 1) { printer.displayLn(match { it.toString().contains("Invalid date format") }) }
    }

    @Test
    fun `promptSelectionIndex should should return Int`() = runTest {
        // Given
        val expectedRes = 1
        every { reader.readInt() } returns null andThen 11 andThen expectedRes
        // When
        val res = promptService.promptSelectionIndex("inter String", 10)
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes - 1)
        verify(exactly = 2) { printer.displayLn(match { it.toString().contains("enter a number between 1") }) }
    }

    @Test
    fun `promptString should should return the current string when not entering`() = runTest {
        // Given
        val expectedRes = "name"
        every { reader.readString() } returns null
        // When
        val res = promptService.promptString("inter String", expectedRes)
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
    }

    @Test
    fun `promptSelectionIndex should should return the currentValue when not entering`() = runTest {
        // Given
        val expectedRes = 1
        every { reader.readInt() } returns null
        // When
        val res = promptService.promptSelectionIndex("inter String", 2, expectedRes)
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
    }

    @Test
    fun `promptSelectionIndex should should return the valid entered value`() = runTest {
        // Given
        val expectedRes = 1
        every { reader.readInt() } returns 4 andThen 0 andThen expectedRes + 1
        // When
        val res = promptService.promptSelectionIndex("inter String", 3, 2)
        // Then
        Truth.assertThat(res).isEqualTo(expectedRes)
        verify(exactly = 2) { printer.displayLn(match { it.toString().contains("enter a number between 1") }) }
    }
}