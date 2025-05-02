package presentation.audit

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.InvalidAudit
import squad.abudhabi.logic.repository.AuditRepository
import squad.abudhabi.presentation.ui_io.ConsolePrinter
import squad.abudhabi.presentation.ui_io.InputReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class AuditUiTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var reader: InputReader
    private lateinit var createAuditUI: CreateAuditUI
    private val outContent = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        reader = mockk()
        createAuditUI = CreateAuditUI(auditRepository, ConsolePrinter(), reader)
        System.setOut(PrintStream(outContent))
    }

    @Test
    fun `should create audit log successfully`() {
        // Given
        every { reader.readString() } returnsMany listOf(
            "abc123", "v1", "v2", "admin", "p"
        )

        // When
        createAuditUI.launchUi()
        val output = outContent.toString()

        // Then
        assertTrue(output.contains("✅ Audit log created successfully."))
    }

    @Test
    fun `should display invalid entity type message`() {
        // Given
        every { reader.readString() } returnsMany listOf(
            "abc123", "v1", "v2", "admin", "x"
        )

        // When
        createAuditUI.launchUi()
        val output = outContent.toString()

        // Then
        assertTrue(output.contains("❌ Invalid input. Please enter 'p' for PROJECT or 't' for TASK."))
    }

    @Test
    fun `should show error for invalid audit`() {
        // Given
        every { reader.readString() } returnsMany listOf(
            "abc123", "same", "same", "admin", "p"
        )
        every { auditRepository.createAuditLog(any()) } throws InvalidAudit()

        // When
        createAuditUI.launchUi()
        val output = outContent.toString()

        // Then
        assertTrue(output.contains("❌ Failed to create audit log:"))
    }

    @Test
    fun `should show generic error for unexpected exception`() {
        // Given
        every { reader.readString() } returnsMany listOf(
            "abc123", "old", "new", "admin", "t"
        )
        every { auditRepository.createAuditLog(any()) } throws RuntimeException("DB error")

        // When
        createAuditUI.launchUi()
        val output = outContent.toString()

        // Then
        assertTrue(output.contains("❌ Unexpected error:"))
    }
}
