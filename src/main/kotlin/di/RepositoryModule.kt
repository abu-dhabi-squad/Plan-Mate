package di

import data.audit.AduitMapper.AuditMapper
import data.audit.datasource.AuditDataSource
import data.audit.datasource.csvdatasource.CsvAuditDataSource
import data.audit.datasource.mongodatabase.MongoAuditDataSource
import data.audit.repository.AuditRepositoryImpl
import data.authentication.UserMapper
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
import data.task.repository.TaskRepositoryImpl
import squad.abudhabi.data.authentication.datasource.InMemoryLoggedUserDataSource
import data.authentication.datasource.LoggedUserDataSource
import data.authentication.datasource.MongoAuthenticationDataSource
import data.project.datasource.mongo_datasource.MongoProjectDataSource
import data.project.mapper.ProjectMapper
import data.task.datasource.csv_datasource.CsvTaskDataSource
import data.task.datasource.mongo_datasource.MongoTaskDataSource
import data.task.mapper.TaskMapper
import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.data.task.parser.CsvTaskParser
import squad.abudhabi.data.task.parser.TaskParser
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import org.koin.core.qualifier.named
import squad.abudhabi.logic.repository.ProjectRepository
import squad.abudhabi.logic.repository.TaskRepository

val repositoryModule = module {
    single { ProjectMapper() }
    single { TaskMapper() }
    single { AuditMapper() }
    single { UserMapper() }

    single<FileHelper> { CsvFileHelper() }

    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<AuditRepository> { AuditRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }

    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single<TaskParser> { CsvTaskParser(get()) }
    single<AuditParser> { CsvAuditParser(get()) }

    single<AuthenticationDataSource> { CsvAuthenticationDataSource(get(), get(), "auth.csv") }
    single<LoggedUserDataSource> { InMemoryLoggedUserDataSource() }
    //    single<ProjectDataSource> {  CsvProjectDataSource(get(),get(),"project.csv")}
    //    single<TaskDataSource> { CsvTaskDataSource(get(),"task.csv",get())  }
    //    single<AuditDataSource> { CsvAuditDataSource(get(),"audit.csv",get() ) }

    //  single<AuthenticationDataSource> { MongoAuthenticationDataSource(get(named("users")), get()) }
    single<ProjectDataSource> {
        MongoProjectDataSource(get(named("projects")), get())
    }
    single<TaskDataSource> {
        MongoTaskDataSource(get(named("tasks")), get())
    }
    single<AuditDataSource> {
        MongoAuditDataSource(get(named("audits")), get())
    }

}