package presentation.audit

import GetAuditUseCase
import squad.abudhabi.logic.exceptions.EmptyList
import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class GetAuditUI(
    private val auditRepository: AuditRepository,
    private val printer: Printer,
    private val reader: InputReader
) : UiLauncher {

    private val getAuditUseCase = GetAuditUseCase(auditRepository)

    override fun launchUi() {

        printer.display("=== Retrieve Audit Logs ===")
        printer.display("Enter entity ID to retrieve audit logs: ")
        val entityId = reader.readString()?.trim()

        if (entityId.isNullOrEmpty()) {
            printer.display("❌ Entity ID cannot be empty.")
            return
        }

        try {

            val audits: List<Audit> = getAuditUseCase(entityId)
            printer.display("=== Audit Logs ===")
            audits.forEachIndexed { index, audit ->
                printer.display("${index + 1}. Entity: ${audit.entityId}, Created By: ${audit.createdBy}, Date: ${audit.date}")
            }

        } catch (e: WrongInputException) {
            printer.display(e.message)
        } catch (e: EmptyList) {
            printer.display(e.message)
        } catch (e: Exception) {
            printer.display("❌ Unexpected error: ${e.message}")
        }
    }
}
