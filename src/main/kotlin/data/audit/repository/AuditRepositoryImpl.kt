package data.audit.repository

import data.audit.mapper.AuditLogMapper
import logic.model.Audit
import logic.repository.AuditRepository
import data.utils.BaseRepository
import java.util.UUID

class AuditRepositoryImpl(
    private val remoteAuditDataSource: RemoteAuditDataSource,
    private val auditLogMapper: AuditLogMapper
) : AuditRepository, BaseRepository() {

    override suspend fun createAuditLog(auditLog: Audit) = wrapResponse {
        remoteAuditDataSource.createAuditLog(auditLogMapper.auditToDto(auditLog))
    }

    override suspend fun getAuditByEntityId(entityId: UUID): List<Audit> = wrapResponse {
        remoteAuditDataSource.getAuditByEntityId(entityId.toString())
            .map { auditLogMapper.dtoToAudit(it) }
    }

}