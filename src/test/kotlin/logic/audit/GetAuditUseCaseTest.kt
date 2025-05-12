package logic.audit

import com.google.common.truth.Truth.assertThat
import helper.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.model.EntityType
import logic.repository.AuditRepository
import org.junit.jupiter.api.assertThrows
import java.util.*
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
    fun `should returns audit list when entityId is valid`() = runTest{

        // given
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

        // when
        val result = getAuditUseCase(entityId)

        // then
        assertThat(expectedAudits).isEqualTo(result)
    }

    @Test
    fun `should throws exception when repository fail`()= runTest {

        // given
        val entityId = UUID.randomUUID()
        coEvery { auditRepository.getAuditByEntityId(entityId) } throws Exception()

        // then
        assertThrows<Exception>() { getAuditUseCase(entityId) }
    }

    @Test
    fun `should throws empty list exception when there is no audit history`()= runTest {

        // given
        val entityId = UUID.randomUUID()

        coEvery { auditRepository.getAuditByEntityId(entityId) } returns emptyList()

        // then
        assertFails { getAuditUseCase(entityId) }
    }

}