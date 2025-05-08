package di

import data.audit.mapper.AuditMapper
import data.audit.model.AuditDto
import data.audit.repository.AuditRepositoryImpl
import data.authentication.mapper.UserMapper
import data.authentication.datasource.csv_datasource.CsvUserParser
import data.authentication.repository.AuthenticationRepositoryImpl
import data.audit.datasource.csvdatasource.csvparser.AuditParser
import org.koin.core.qualifier.named

import data.audit.datasource.csvdatasource.csvparser.CsvAuditParser
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.kotlin.client.coroutine.MongoClient
import data.audit.datasource.mongo_database.MongoAuditDataSource
import data.audit.datasource.mongo_database.RemoteAuditDataSource
import data.authentication.datasource.mongo_datasource.RemoteAuthenticationDataSource
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectParser
import data.project.repository.ProjectRepositoryImpl
import data.task.repository.TaskRepositoryImpl
import data.authentication.datasource.localdatasource.InMemoryLoggedUserDataSource
import data.authentication.datasource.localdatasource.LoggedUserDataSource
import data.authentication.datasource.mongo_datasource.MongoAuthenticationDataSource
import data.authentication.model.UserDto
import data.project.datasource.mongo_datasource.MongoProjectDataSource
import data.project.datasource.mongo_datasource.RemoteProjectDataSource
import data.project.mapper.ProjectMapper
import data.project.model.ProjectDto
import data.task.datasource.mongo_datasource.MongoTaskDataSource
import data.task.datasource.mongo_datasource.RemoteTaskDataSource
import data.task.mapper.TaskMapper
import data.task.model.TaskDto
import data.task.parser.CsvTaskParser
import data.task.parser.TaskParser
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import logic.repository.ProjectRepository
import logic.repository.TaskRepository

val repositoryModule = module {
    single { ProjectMapper() }
    single { TaskMapper() }
    single { AuditMapper() }
    single { UserMapper() }

    single<FileHelper> { CsvFileHelper() }

    single<ProjectRepository> { ProjectRepositoryImpl(get(),get()) }
    single<RemoteTaskDataSource> { MongoTaskDataSource(get(named("task"))) }
    single<TaskRepository> { TaskRepositoryImpl(get(),get()) }
    single<AuditRepository> { AuditRepositoryImpl(get(),get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(),get()) }

    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single<TaskParser> { CsvTaskParser(get()) }
    single<AuditParser> { CsvAuditParser(get()) }

    single<LoggedUserDataSource> { InMemoryLoggedUserDataSource() }

    single<RemoteAuthenticationDataSource> { MongoAuthenticationDataSource(get(named("users"))) }
    single<RemoteProjectDataSource> {
        MongoProjectDataSource(get(named("projects")))
    }
    single<RemoteTaskDataSource> {
        MongoTaskDataSource(get(named("tasks")))
    }
    single<RemoteAuditDataSource> {
        MongoAuditDataSource(get(named("audits")))
    }

    single {
        val client:MongoClient = MongoClient.create("mongodb+srv://abudhabi:DhXobvmTriIWl9y5@planmate.sj0lbdm.mongodb.net/?retryWrites=true&w=majority&appName=PlanMate")
        client.getDatabase("PlanMate")
    }

    single(named("audits")) {
        val database: MongoDatabase = get()
        database.getCollection<AuditDto>("audits")
    }

    single(named("projects")) {
        val database: MongoDatabase = get()
        database.getCollection<ProjectDto>("projects")
    }
    single(named("tasks")) {
        val database: MongoDatabase = get()
        database.getCollection<TaskDto>("tasks")
    }

    single(named("users")) {
        val database: MongoDatabase = get()
        database.getCollection<UserDto>("users")
    }


}