package logic.audit

import createAudit
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.exceptions.WrongInputException
import logic.model.EntityType
import logic.repository.AuditRepository
import kotlin.test.assertFails

class GetAuditUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditUseCase: GetAuditUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        getAuditUseCase = GetAuditUseCase(auditRepository)
    }

    @Test
    fun `get Audit History should returns audit list when entityId is valid`() {

        // given
        val entityId = "123"
        val expectedAudits = listOf(
            createAudit(
                entityId = entityId,
                entityType = EntityType.PROJECT
            ),
            createAudit(
                entityId = entityId,
            )
        )

        every { auditRepository.getAuditByEntityId(entityId) } returns expectedAudits

        // when
        val result = getAuditUseCase(entityId)

        // then
        assertEquals(expectedAudits, result)
    }

    @Test
    fun `get Audit History throws WrongInputException when entityId is empty`() {

        // given
        val entityId = ""
        every { auditRepository.getAuditByEntityId(entityId) } throws WrongInputException()

        // then
        assertFails { getAuditUseCase(entityId) }
    }

    @Test
    fun `get Audit History throws Empty List Exception when there is no Audit History`() {

        // given
        val entityId = "UG7299"

        every { auditRepository.getAuditByEntityId(entityId) } returns emptyList()

        // then
        assertFails { getAuditUseCase(entityId) }
    }

}