package di

import data.audit.mapper.MongoAuditMapper
import data.audit.model.AuditDto
import data.audit.repository.AuditRepositoryImpl
import data.authentication.mapper.MongoUserMapper
import data.authentication.datasource.csv.CsvUserParser
import data.authentication.repository.AuthenticationRepositoryImpl
import data.audit.datasource.csv.parser.AuditParser
import org.koin.core.qualifier.named

import data.audit.datasource.csv.parser.CsvAuditParser
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.kotlin.client.coroutine.MongoClient
import data.audit.datasource.mongo.MongoAudit
import data.audit.repository.RemoteAuditDataSource
import data.authentication.repository.RemoteAuthenticationDataSource
import org.koin.dsl.module
import data.project.datasource.csv.CsvProjectParser
import data.project.repository.ProjectRepositoryImpl
import data.task.repository.TaskRepositoryImpl
import data.authentication.datasource.inmemory.InMemoryLoggedUser
import data.authentication.repository.LoggedUserDataSource
import data.authentication.datasource.mongo.MongoAuthentication
import data.authentication.model.UserDto
import data.project.datasource.mongo.MongoProject
import data.project.repository.RemoteProjectDataSource
import data.project.mapper.MongoProjectMapper
import data.project.model.ProjectDto
import data.task.datasource.csv.CsvTaskParser
import data.task.datasource.mongo.MongoTask
import data.task.repository.RemoteTaskDataSource
import data.task.mapper.MongoTaskMapper
import data.task.model.TaskDto
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import logic.repository.ProjectRepository
import logic.repository.TaskRepository

val repositoryModule = module {
    single { MongoProjectMapper() }
    single { MongoTaskMapper() }
    single { MongoAuditMapper() }
    single { MongoUserMapper() }

    single<FileHelper> { CsvFileHelper() }

    single<ProjectRepository> { ProjectRepositoryImpl(get(),get()) }
    single<RemoteTaskDataSource> { MongoTask(get(named("task"))) }
    single<TaskRepository> { TaskRepositoryImpl(get(),get()) }
    single<AuditRepository> { AuditRepositoryImpl(get(),get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(),get(),get()) }

    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single { CsvTaskParser(get()) }
    single<AuditParser> { CsvAuditParser(get()) }

    single<LoggedUserDataSource> { InMemoryLoggedUser() }

    single<RemoteAuthenticationDataSource> { MongoAuthentication(get(named("users"))) }
    single<RemoteProjectDataSource> {
        MongoProject(get(named("projects")))
    }
    single<RemoteTaskDataSource> {
        MongoTask(get(named("tasks")))
    }
    single<RemoteAuditDataSource> {
        MongoAudit(get(named("audits")))
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