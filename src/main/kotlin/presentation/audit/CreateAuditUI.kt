package presentation.audit

import logic.audit.CreateAuditUseCase
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.repository.AuditRepository
import squad.abudhabi.logic.exceptions.InvalidAudit
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

import java.time.LocalDate
import java.util.UUID

class CreateAuditUI(
    private val auditRepository: AuditRepository,
    private val printer: Printer,
    private val reader: InputReader
) : UiLauncher {

    private val createAuditUseCase = CreateAuditUseCase(auditRepository)

    override fun launchUi() {

        printer.display("=== Create New Audit Log ===")

        printer.display("Enter entity ID: ")
        val entityId = reader.readString()
        printer.display("Enter old state: ")
        val oldState = reader.readString()
        printer.display("Enter new state: ")
        val newState = reader.readString()
        printer.display("Enter created by: ")
        val createdBy = reader.readString()
        printer.display("Enter entity type (p for PROJECT, t for TASK): ")
        val entityTypeInput = reader.readString()

        val entityType = when (entityTypeInput) {
            "p" -> EntityType.PROJECT
            "t" -> EntityType.TASK
            else -> {
                printer.display("❌ Invalid input. Please enter 'p' for PROJECT or 't' for TASK.")
                return
            }
        }

        val audit = Audit(
            id = UUID.randomUUID(),
            createdBy = createdBy.toString(),
            entityId = entityId.toString(),
            entityType = entityType,
            oldState = oldState.toString(),
            newState = newState.toString(),
            date = LocalDate.now()
        )

        try {
            createAuditUseCase(audit)
            printer.display("✅ Audit log created successfully.")
        } catch (e: InvalidAudit) {
            printer.display("❌ Failed to create audit log: ${e.message}")
        } catch (e: Exception) {
            printer.display("❌ Unexpected error: ${e.message}")
        }
    }
}
