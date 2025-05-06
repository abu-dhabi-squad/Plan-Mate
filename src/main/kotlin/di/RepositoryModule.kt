package di

import data.audit.datasource.AuditDataSource
import data.audit.datasource.CsvAuditDataSource
import data.audit.repository.AuditRepositoryImpl
import data.authentication.datasource.AuthenticationDataSource
import data.authentication.datasource.CsvAuthenticationDataSource
import data.authentication.datasource.CsvUserParser
import data.authentication.repository.AuthenticationRepositoryImpl
import data.parser.AuditParser
import data.parser.CsvAuditParser
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectDataSource
import data.project.datasource.csv_datasource.CsvProjectParser
import data.project.datasource.ProjectDataSource
import data.project.repository.ProjectRepositoryImpl
import data.task.datasource.csv_datasource.CsvTaskDataSource
import data.task.repository.TaskRepositoryImpl
import squad.abudhabi.data.authentication.datasource.InMemoryLoggedUserDataSource
import data.authentication.datasource.LoggedUserDataSource
import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.data.task.parser.CsvTaskParser
import squad.abudhabi.data.task.parser.TaskParser
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import squad.abudhabi.logic.repository.ProjectRepository
import squad.abudhabi.logic.repository.TaskRepository

val repositoryModule = module {
    single<AuditRepository> { AuditRepositoryImpl(get()) }
    single<AuditDataSource> { CsvAuditDataSource(get(),"audit.csv",get() ) }
    single<AuthenticationDataSource>{ CsvAuthenticationDataSource(get(),get(),"auth.csv") }
    single<CsvUserParser>{ CsvUserParser() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(),get())  }
    single <LoggedUserDataSource>{  InMemoryLoggedUserDataSource() }
    single<AuditParser>{CsvAuditParser(get())}
    single<ProjectDataSource> {  CsvProjectDataSource(get(),get(),"project.csv")}
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single { CsvProjectParser() }
    single<TaskDataSource> { CsvTaskDataSource(get(),"task.csv",get())  }
    single<TaskParser> { CsvTaskParser(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<FileHelper> { CsvFileHelper() }
}