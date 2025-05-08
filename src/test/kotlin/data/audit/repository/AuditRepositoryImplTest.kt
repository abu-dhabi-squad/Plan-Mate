package data.audit.repository

import com.google.common.truth.Truth
import createAudit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.audit.datasource.AuditDataSource
import logic.repository.AuditRepository
import kotlin.test.assertFails
import kotlin.test.assertTrue
/*class AuditRepositoryImplTest {

    lateinit var dataSource: RemoteAuditDataSource
    lateinit var auditMapper: AuditMapper
    lateinit var auditRepository: AuditRepository


    @BeforeEach
    fun setup(){
        dataSource = mockk(relaxed = true)
        auditMapper = mockk(relaxed = true)
        auditRepository = AuditRepositoryImpl(dataSource,auditMapper)
    }


    @Test
    fun `addAudit should call addAudit once when data source is not empty`() = runTest{

        // given
        val audit = createAudit()

        // when
        //dataSource.createAuditLog(audit)

        // then
        coVerify(exactly = 1){ dataSource.createAuditLog(any()) }
    }

    @Test
    fun `addAudit should rethrow exception when data source throw exception`() = runTest{

        // given
        val audit = createAudit()

        coEvery { dataSource.createAuditLog(any()) } throws Exception()

        // when & then
        assertFails { auditRepository.createAuditLog(audit) }
    }


    @Test
    fun `getAuditByEntityId should return list of audits that match entity id`()= runTest{

        // given
        val audits = listOf(
            createAudit(),
            createAudit()
        )

        //coEvery { dataSource.getAuditByEntityId(any()) } returns audits

        // when
        val result = auditRepository.getAuditByEntityId(ENTITY_ID)

        // then
        Truth.assertThat(result).containsExactly(*audits.toTypedArray())
    }



    @Test
    fun `getAuditByEntityId should return empty list when data source is empty`()= runTest {

        // given
        coEvery { dataSource.getAuditByEntityId(any()) } returns emptyList()

        // when & then
        assertTrue { auditRepository.getAuditByEntityId(ENTITY_ID).isEmpty() }
    }

    @Test
     fun `getAuditByEntityId should rethrow exception when dataSource throw Exception`() = runTest{

        // given
        coEvery { dataSource.getAuditByEntityId(any()) } throws Exception()

        // when & then
        assertFails { auditRepository.getAuditByEntityId(ENTITY_ID) }
    }

    companion object {
        private const val ENTITY_ID = "UG7299"
    }
}*/