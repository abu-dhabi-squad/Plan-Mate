package logic.audit

import createAudit
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.repository.AuditRepository
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
    fun `addAudit adds valid audit`()= runTest {

        // given
        val audit = createAudit(
            entityId = "asdww98"
        )

        // when
        createAuditUseCase(audit)

        // then

        coVerify(exactly = 1) {
            auditRepository.createAuditLog(match {
                it.id == audit.id &&
                        it.createdBy == audit.createdBy &&
                        it.entityId == audit.entityId &&
                        it.entityType == audit.entityType &&
                        it.oldState == audit.oldState &&
                        it.newState == audit.newState &&
                        it.date == audit.date
            })
        }
    }

    @ParameterizedTest
    @CsvSource(
        "audit1,'',new",
        "'',entity1,new",
        "audit1,entity1,''"
    )
    fun `addAudit throws InvalidAudit when essential param is empty`(
        entityId: String,
        createdBy: String,
        newState: String
    )= runTest {

        // given
        val audit = createAudit(
            entityId = entityId,
            createdBy = createdBy,
            newState = newState
        )

        // then
        assertFails {
            createAuditUseCase(audit)
        }

    }

    @Test
    fun `addAudit throws InvalidAudit when newState equals oldState`() = runTest{

        // given
        val audit = createAudit(
            entityId = "asdww98",
            newState = "done",
            oldState = "done"
        )

        // then
        assertFails {
            createAuditUseCase(audit)
        }
    }
}