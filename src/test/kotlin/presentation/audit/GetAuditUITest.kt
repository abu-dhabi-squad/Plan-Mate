//package presentation.audit
//
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import squad.abudhabi.logic.exceptions.EmptyList
//import squad.abudhabi.logic.exceptions.WrongInputException
//import squad.abudhabi.logic.model.Audit
//import squad.abudhabi.logic.model.EntityType
//import squad.abudhabi.logic.repository.AuditRepository
//import squad.abudhabi.presentation.ui_io.ConsolePrinter
//import squad.abudhabi.presentation.ui_io.InputReader
//import java.io.ByteArrayOutputStream
//import java.io.PrintStream
//import java.time.LocalDate
//import java.util.UUID
//import kotlin.test.assertTrue
//
//class GetAuditUITest {
//
//    private lateinit var auditRepository: AuditRepository
//    private lateinit var reader: InputReader
//    private lateinit var getAuditUI: GetAuditUI
//    private val outContent = ByteArrayOutputStream()
//
//    @BeforeEach
//    fun setUp() {
//        auditRepository = mockk()
//        reader = mockk()
//        getAuditUI = GetAuditUI(auditRepository, ConsolePrinter(), reader)
//        System.setOut(PrintStream(outContent))
//    }
//
//    @Test
//    fun `should display audits when found`() {
//        // Given
//        val entityId = "123"
//        val audits = listOf(
//            Audit(
//                id = UUID.randomUUID(),
//                createdBy = "admin",
//                entityId = entityId,
//                entityType = EntityType.PROJECT,
//                oldState = "draft",
//                newState = "done",
//                date = LocalDate.now()
//            )
//        )
//        every { reader.readString() } returns entityId
//        every { auditRepository.getAuditByEntityId(entityId) } returns audits
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("=== Audit Logs ==="))
//        assertTrue(output.contains("Entity: 123"))
//    }
//
//    @Test
//    fun `should display error when entityId is empty`() {
//        // Given
//        every { reader.readString() } returns ""
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("❌ Entity ID cannot be empty."))
//    }
//
//    @Test
//    fun `should display error when entityId is null`() {
//        // Given
//        every { reader.readString() } returns null
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("❌ Entity ID cannot be empty."))
//    }
//
//    @Test
//    fun `should handle WrongInputException`() {
//        // Given
//        val entityId = "invalid"
//        every { reader.readString() } returns entityId
//        every { auditRepository.getAuditByEntityId(entityId) } throws WrongInputException()
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("❌ Invalid entity ID input"))
//    }
//
//    @Test
//    fun `should handle EmptyList exception`() {
//        // Given
//        val entityId = "no-results"
//        every { reader.readString() } returns entityId
//        every { auditRepository.getAuditByEntityId(entityId) } throws EmptyList()
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("❌ No audit logs found for this entity ID."))
//    }
//
//    @Test
//    fun `should handle unexpected exception`() {
//        // Given
//        val entityId = "any"
//        every { reader.readString() } returns entityId
//        every { auditRepository.getAuditByEntityId(entityId) } throws RuntimeException("DB down")
//
//        // When
//        getAuditUI.launchUi()
//        val output = outContent.toString()
//
//        // Then
//        assertTrue(output.contains("❌ Unexpected error:"))
//    }
//}
