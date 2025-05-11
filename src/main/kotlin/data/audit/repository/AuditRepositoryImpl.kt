package data.audit.repository

import data.audit.mapper.AuditMapper
import logic.model.Audit
import logic.repository.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    private val remoteAuditDataSource: RemoteAuditDataSource,
    private val auditMapper: AuditMapper
) : AuditRepository {

    override suspend fun createAuditLog(auditLog: Audit) {
        remoteAuditDataSource.createAuditLog(auditMapper.auditToDto(auditLog))
    }

    override suspend fun getAuditByEntityId(entityId: UUID): List<Audit> {
        return remoteAuditDataSource.getAuditByEntityId(entityId.toString()).map { auditMapper.dtoToAudit(it) }
    }
}