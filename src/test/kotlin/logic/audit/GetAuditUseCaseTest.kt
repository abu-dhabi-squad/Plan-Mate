package logic.audit

import GetAuditUseCase
import createAudit
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.repository.AuditRepository
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
                id = "0",
                entityType = EntityType.PROJECT
            ),
            createAudit(
                entityId = entityId,
                id = "9"
            )
        )

        // when
        every { auditRepository.getAuditByEntityId(entityId) } returns expectedAudits

        val result = getAuditUseCase.getAuditHistory(entityId)

        // then
        assertEquals(expectedAudits, result)
    }

    @Test
    fun `get Audit History throws WrongInputException when entityId is empty`() {

        // given
        val entityId = ""

        // when
        every { auditRepository.getAuditByEntityId(entityId) } throws WrongInputException()

        // then
        assertFails { getAuditUseCase.getAuditHistory(entityId) }
    }


}