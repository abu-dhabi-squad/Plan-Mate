//package squad.abudhabi.data.audit.repository
//
//import squad.abudhabi.logic.model.Audit
//import squad.abudhabi.logic.model.EntityType
//import squad.abudhabi.logic.repository.AuditRepository
//import java.io.File
//
//class CsvAuditRepository(private val filePath: String)  : AuditRepository  {
//
////       override fun addAuditLog(audit: Audit) {
////
////        val csvLine = listOf(
////            audit.id,
////            audit.createdBy,
////            audit.entityId,
////            audit.entityType.name,
////            audit.oldState,
////            audit.newState,
////            audit.date
////        ).joinToString(",")
////
////        File(filePath).appendText("$csvLine\n")
////    }
////
////    override fun getAuditByEntityId(entityId: String): List<Audit> {
////
////        return File(filePath).readLines()
////            .mapNotNull { line ->
////                val parts = line.split(",")
////                if (parts.size == 7 && parts[3] == entityId) {
////                    Audit(
////                        id = parts[0],
////                        createdBy = parts[2],
////                        entityId = parts[3],
////                        entityType = EntityType.valueOf(parts[4]),
////                        oldState = parts[5],
////                        newState = parts[6],
////                        date = parts[7]
////                    )
////                } else null
////            }
////    }
//
//    override fun addAuditLog(auditLog: Audit) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAuditByEntityId(entityId: String): List<Audit> {
//        TODO("Not yet implemented")
//    }
//}
