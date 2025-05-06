package di

import data.audit.auditmapper.AuditMapper
import data.audit.datasource.AuditDataSource
import data.audit.datasource.mongodatabase.MongoAuditDataSource
import data.audit.repository.AuditRepositoryImpl
import data.authentication.usermapper.UserMapper
import data.authentication.datasource.AuthenticationDataSource
import data.authentication.datasource.csv_datasource.CsvAuthenticationDataSource
import data.authentication.datasource.csv_datasource.CsvUserParser
import data.authentication.repository.AuthenticationRepositoryImpl
import data.parser.AuditParser
import data.parser.CsvAuditParser
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectParser
import data.project.datasource.ProjectDataSource
import data.project.repository.ProjectRepositoryImpl
import data.task.repository.TaskRepositoryImpl
import data.authentication.datasource.localdatasource.InMemoryLoggedUserDataSource
import data.authentication.datasource.localdatasource.LoggedUserDataSource
import data.project.datasource.mongo_datasource.MongoProjectDataSource
import data.project.projectmapper.ProjectMapper
import data.task.datasource.mongo_datasource.MongoTaskDataSource
import data.task.taskmapper.TaskMapper
import data.task.datasource.TaskDataSource
import data.task.parser.CsvTaskParser
import data.task.parser.TaskParser
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import org.koin.core.qualifier.named
import logic.repository.ProjectRepository
import logic.repository.TaskRepository

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