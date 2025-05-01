package logic.audit

import createAudit
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.repository.AuditRepository
import kotlin.test.assertFails

class AddAuditUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var useCase: AddAuditUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        useCase = AddAuditUseCase(auditRepository)
    }

    @Test
    fun `addAudit adds valid audit`() {

        // given
        val audit = createAudit(
            id = "213",
            entityId = "asdww98"
        )

        // when
        useCase.addAudit(audit)

        // then
        verify(exactly = 1) {
            auditRepository.addAuditLog(match {
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
        "audit1,'',old,new",
        "audit1,entity1,'',new",
        "audit1,entity1,old,''"
    )
    fun `addAudit throws InvalidAudit when essential param is empty`(
        id: String,
        entityId: String,
        createdBy: String,
        newState: String
    ) {

        // given
        val audit = createAudit(
            id = id,
            entityId = entityId,
            createdBy = createdBy,
            newState = newState
        )

        // then
        assertFails {
            useCase.addAudit(audit)
        }

    }

    @Test
    fun `addAudit throws InvalidAudit when newState equals oldState`() {

        // given
        val audit = createAudit(
            id = "213",
            entityId = "asdww98",
            newState = "done",
            oldState = "done"
        )

        // then
        assertFails {
            useCase.addAudit(audit)
        }
    }
}