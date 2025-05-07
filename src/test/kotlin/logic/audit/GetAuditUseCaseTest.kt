package logic.audit

import createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `get Audit History should returns audit list when entityId is valid`() = runTest{

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

        coEvery { auditRepository.getAuditByEntityId(entityId) } returns expectedAudits

        // when
        val result = getAuditUseCase(entityId)

        // then
        assertEquals(expectedAudits, result)
    }

    @Test
    fun `get Audit History throws WrongInputException when entityId is empty`()= runTest {

        // given
        val entityId = ""
        coEvery { auditRepository.getAuditByEntityId(entityId) } throws WrongInputException()

        // then
        assertFails { getAuditUseCase(entityId) }
    }

    @Test
    fun `get Audit History throws Empty List Exception when there is no Audit History`()= runTest {

        // given
        val entityId = "UG7299"

        coEvery { auditRepository.getAuditByEntityId(entityId) } returns emptyList()

        // then
        assertFails { getAuditUseCase(entityId) }
    }

}