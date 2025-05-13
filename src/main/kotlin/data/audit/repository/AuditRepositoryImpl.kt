package data.audit.repository

import data.audit.mapper.AuditMapper
import logic.model.Audit
import logic.repository.AuditRepository
import presentation.data.utils.BaseRepository
import java.util.UUID

class AuditRepositoryImpl(
    private val remoteAuditDataSource: RemoteAuditDataSource,
    private val auditMapper: AuditMapper
) : AuditRepository, BaseRepository() {

    override suspend fun createAuditLog(auditLog: Audit) = wrapResponse {
        remoteAuditDataSource.createAuditLog(auditMapper.auditToDto(auditLog))
    }


    override suspend fun getAuditByEntityId(entityId: UUID): List<Audit> = wrapResponse {
        remoteAuditDataSource.getAuditByEntityId(entityId.toString())
            .map { auditMapper.dtoToAudit(it) }
    }

}