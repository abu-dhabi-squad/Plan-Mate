package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.data.audit.repository.CsvAuditRepository
import squad.abudhabi.logic.audit.AddAuditUseCase
import squad.abudhabi.logic.audit.GetAuditUseCase
import squad.abudhabi.logic.repository.AuditRepository

val repositoryModule = module {

    single<AuditRepository> { CsvAuditRepository("") }
    single { AddAuditUseCase(get()) }
    single { GetAuditUseCase(get()) }
}