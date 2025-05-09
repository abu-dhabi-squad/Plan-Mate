package data.audit.repository

import com.google.common.truth.Truth
import createAudit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.audit.mapper.MongoAuditMapper
import data.audit.model.AuditDto
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.repository.AuditRepository
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AuditRepositoryImplTest {

    lateinit var dataSource: RemoteAuditDataSource
    lateinit var auditMapper: MongoAuditMapper
    lateinit var auditRepository: AuditRepository


    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        auditMapper = mockk(relaxed = true)
        auditRepository = AuditRepositoryImpl(dataSource, auditMapper)
    }

    @Test
    fun `getAuditByEntityId should return list of audits that match entity id`() = runTest {
        // given
        val audit1 = createAudit()
        val audit2 = createAudit()

        val auditDto1 = mockk<AuditDto>()
        val auditDto2 = mockk<AuditDto>()

        every { auditMapper.auditToDto(audit1) } returns auditDto1
        every { auditMapper.auditToDto(audit2) } returns auditDto2

        coEvery { dataSource.getAuditByEntityId(ENTITY_ID) } returns listOf(auditDto1, auditDto2)

        every { auditMapper.dtoToAudit(auditDto1) } returns audit1
        every { auditMapper.dtoToAudit(auditDto2) } returns audit2

        // when
        val result = auditRepository.getAuditByEntityId(ENTITY_ID)

        // then
        Truth.assertThat(result).containsExactly(audit1, audit2)
    }

    @Test
    fun `addAudit should call addAudit once when data source is not empty`() = runTest {
        // given
        val audit = createAudit()
        val auditDto = mockk<AuditDto>()

        every { auditMapper.auditToDto(audit) } returns auditDto
        coEvery { dataSource.createAuditLog(auditDto) } just Runs

        // when
        auditRepository.createAuditLog(audit)

        // then
        coVerify(exactly = 1) { dataSource.createAuditLog(auditDto) }
        verify { auditMapper.auditToDto(audit) }
    }

    @Test
    fun `addAudit should rethrow exception when data source throw exception`() = runTest {
        // given
        val audit = createAudit()

        coEvery { dataSource.createAuditLog(any()) } throws Exception()

        // when & then
        assertFails { auditRepository.createAuditLog(audit) }
    }

    @Test
    fun `getAuditByEntityId should return empty list when data source is empty`() = runTest {
        // given
        coEvery { dataSource.getAuditByEntityId(any()) } returns emptyList()

        // when & then
        assertTrue { auditRepository.getAuditByEntityId(ENTITY_ID).isEmpty() }
    }

    @Test
    fun `getAuditByEntityId should rethrow exception when dataSource throw Exception`() = runTest {
        // given
        coEvery { dataSource.getAuditByEntityId(any()) } throws Exception()

        // when & then
        assertFails { auditRepository.getAuditByEntityId(ENTITY_ID) }
    }

    @Test
    fun `createAuditLog should handle audit with minimal data`() = runTest {
        // given
        val audit = createAudit().copy(
            oldState = "",
            newState = "",
            createdBy = "",
        )
        val auditDto = mockk<AuditDto>()

        every { auditMapper.auditToDto(audit) } returns auditDto
        coEvery { dataSource.createAuditLog(auditDto) } just Runs

        // when
        auditRepository.createAuditLog(audit)

        // then
        coVerify { dataSource.createAuditLog(auditDto) }
        verify { auditMapper.auditToDto(audit) }
    }

    companion object {
        private const val ENTITY_ID = "UG7299"
    }
}