package logic.audit

import com.google.common.truth.Truth.assertThat
import helper.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.NoAuditsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.model.Audit.EntityType
import logic.repository.AuditRepository
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetAuditUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditUseCase: GetAuditUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        getAuditUseCase = GetAuditUseCase(auditRepository)
    }

    @Test
    fun `should returns audit list when entityId is valid`() = runTest {

        // Given
        val entityId = UUID.randomUUID()
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

        // When
        val result = getAuditUseCase(entityId)

        // Then
        assertThat(expectedAudits).isEqualTo(result)
    }

    @Test
    fun `should throws exception when repository fail`() = runTest {

        // Given
        val entityId = UUID.randomUUID()
        coEvery { auditRepository.getAuditByEntityId(entityId) } throws Exception()

        // When & Then
        assertThrows<Exception>() { getAuditUseCase(entityId) }
    }

    @Test
    fun `should throws NoAuditsFoundException when there is no audit history`() = runTest {

        // Given
        val entityId = UUID.randomUUID()
        coEvery { auditRepository.getAuditByEntityId(entityId) } returns emptyList()

        // When & Then
        assertThrows<NoAuditsFoundException>() { getAuditUseCase(entityId) }
    }

}