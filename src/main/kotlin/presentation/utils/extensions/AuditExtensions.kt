package presentation.utils.extensions

import logic.model.Audit
import presentation.io.Printer

fun Audit.displayAt(index: Int, printer: Printer) {
    printer.displayLn("${index}. Date: $createdAt, Created By: $createdBy")
    if (oldState.isEmpty()) {
        printer.displayLn("\t=> New state set as $newState")
    } else {
        printer.displayLn("\t=> Changed from $oldState to $newState")
    }
}

fun List<Audit>.showAuditLogs(printer: Printer) {
    if (isEmpty()) {
        printer.displayLn("\nNo audit logs found.")
        return
    }
    printer.displayLn("\n=== Audit Logs ===")
    forEachIndexed { idx, audit ->
        audit.displayAt(idx + 1, printer)
    }
    printer.displayLn()
}