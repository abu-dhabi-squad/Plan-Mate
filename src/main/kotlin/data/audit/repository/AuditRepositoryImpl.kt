package data.audit.repository

import data.audit.mapper.MongoAuditMapper
import logic.model.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val remoteAuditDataSource: RemoteAuditDataSource,
    private val mongoAuditMapper: MongoAuditMapper
) : AuditRepository {

    override suspend fun createAuditLog(auditLog: Audit) {
        remoteAuditDataSource.createAuditLog(mongoAuditMapper.auditToDto(auditLog))
    }

    override suspend fun getAuditByEntityId(entityId: String): List<Audit> {
        return remoteAuditDataSource.getAuditByEntityId(entityId).map { mongoAuditMapper.dtoToAudit(it) }
    }
}