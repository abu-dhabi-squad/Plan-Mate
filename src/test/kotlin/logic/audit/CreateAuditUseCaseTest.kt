package logic.audit

import helper.createAudit
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.InvalidAudit
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.UUID

class CreateAuditUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var createAuditUseCase: CreateAuditUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        createAuditUseCase = CreateAuditUseCase(auditRepository)
    }

    @Test
    fun `should adds valid audit when audit is valid`() = runTest {

        // Given
        val audit = createAudit(
            entityId = UUID.randomUUID(),
            newState = "new state",
            oldState = "old state",
            createdBy = "noor"
        )
        // When
        createAuditUseCase(audit)

        // Then
        coVerify(exactly = 1) {
            auditRepository.createAuditLog(match {
                it.auditId == audit.auditId &&
                        it.createdBy == audit.createdBy &&
                        it.entityId == audit.entityId &&
                        it.entityType == audit.entityType &&
                        it.oldState == audit.oldState &&
                        it.newState == audit.newState &&
                        it.createdAt == audit.createdAt
            })
        }
    }

    @ParameterizedTest
    @CsvSource(
        "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,'',new",
        "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,entity1,''"
    )
    fun `should throws InvalidAudit when essential param is empty`(
        entityId: String,
        createdBy: String,
        newState: String
    ) = runTest {

        // Given
        val audit = createAudit(
            entityId = UUID.fromString(entityId),
            createdBy = createdBy,
            newState = newState
        )

        // When & Then
        assertThrows<InvalidAudit> {
            createAuditUseCase(audit)
        }
    }

    @Test
    fun `should throws InvalidAudit when newState equals oldState`() = runTest {

        // Given
        val audit = createAudit(
            entityId = UUID.randomUUID(),
            newState = "done",
            oldState = "done"
        )

        // When & Then
        assertThrows<InvalidAudit> {
            createAuditUseCase(audit)
        }
    }
}