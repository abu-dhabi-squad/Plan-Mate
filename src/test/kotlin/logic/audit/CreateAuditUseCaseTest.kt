package logic.audit

import helper.createAudit
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.InvalidAudit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.repository.AuditRepository
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertFails

class CreateAuditUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var createAuditUseCase: CreateAuditUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        createAuditUseCase = CreateAuditUseCase(auditRepository)
    }

    @Test
    fun `addAudit adds valid audit`() = runTest {

        // given
        val audit = createAudit(
            entityId = UUID.randomUUID(),
        )

        // when
        createAuditUseCase(audit)

        // then

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
    fun `addAudit throws InvalidAudit when essential param is empty`(
        entityId: String,
        createdBy: String,
        newState: String
    ) = runTest {

        // given
        val audit = createAudit(
            entityId = UUID.fromString(entityId),
            createdBy = createdBy,
            newState = newState
        )

        // then
        assertThrows<InvalidAudit> {
            createAuditUseCase(audit)
        }

    }

    @Test
    fun `addAudit throws InvalidAudit when newState equals oldState`() = runTest {

        // given
        val audit = createAudit(
            entityId = UUID.randomUUID(),
            newState = "done",
            oldState = "done"
        )

        // then
        assertFails {
            createAuditUseCase(audit)
        }
    }
}