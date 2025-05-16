package data.audit.repository

import com.google.common.truth.Truth.assertThat
import helper.createAudit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.audit.mapper.AuditLogMapper
import data.audit.model.AuditDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.repository.AuditRepository
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AuditRepositoryImplTest {

    lateinit var dataSource: RemoteAuditDataSource
    lateinit var auditLogMapper: AuditLogMapper
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        auditLogMapper = mockk(relaxed = true)
        auditRepository = AuditRepositoryImpl(dataSource, auditLogMapper)
    }

    @Test
    fun `getAuditByEntityId should return list of audits that match entity id`() = runTest {
        // given
        val audit1 = createAudit()
        val audit2 = createAudit()

        val auditDto1 = mockk<AuditDto>()
        val auditDto2 = mockk<AuditDto>()

        every { auditLogMapper.auditToDto(audit1) } returns auditDto1
        every { auditLogMapper.auditToDto(audit2) } returns auditDto2

        coEvery { dataSource.getAuditByEntityId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString()) } returns listOf(auditDto1, auditDto2)

        every { auditLogMapper.dtoToAudit(auditDto1) } returns audit1
        every { auditLogMapper.dtoToAudit(auditDto2) } returns audit2

        // when
        val result = auditRepository.getAuditByEntityId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))

        // then
        assertThat(result).containsExactly(audit1, audit2)
    }

    @Test
    fun `addAudit should call addAudit once when data source is not empty`() = runTest {
        // given
        val audit = createAudit()
        val auditDto = mockk<AuditDto>()

        every { auditLogMapper.auditToDto(audit) } returns auditDto
        coEvery { dataSource.createAuditLog(auditDto) }just Runs

        // when
        auditRepository.createAuditLog(audit)

        // then
        coVerify(exactly = 1) { dataSource.createAuditLog(auditDto) }
        verify { auditLogMapper.auditToDto(audit) }
    }

    @Test
    fun `addAudit should rethrow exception when data source throw exception`() = runTest {
        // given
        val audit = createAudit()

        coEvery { dataSource.createAuditLog(any()) } throws Exception()

        // when & then
        assertThrows<Exception> {
            auditRepository.createAuditLog(audit)
        }
    }
    @Test
    fun `getAuditByEntityId should return empty list when data source is empty`() = runTest {
        // given
        coEvery { dataSource.getAuditByEntityId(any()) } returns emptyList()

        // when
        val result = auditRepository.getAuditByEntityId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))

        // then
        assertThat(result).isEmpty()
    }


    @Test
    fun `getAuditByEntityId should rethrow exception when dataSource throw Exception`() = runTest {
        // given
        coEvery { dataSource.getAuditByEntityId(any()) } throws Exception()

        // when & then
        assertThrows<Exception> {
            auditRepository.getAuditByEntityId(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))
        }
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

        every { auditLogMapper.auditToDto(audit) } returns auditDto
        coEvery { dataSource.createAuditLog(auditDto) } just Runs

        // when
        auditRepository.createAuditLog(audit)

        // then
        coVerify { dataSource.createAuditLog(auditDto) }
        verify { auditLogMapper.auditToDto(audit) }
    }
}