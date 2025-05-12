package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.audit.datasource.csv.parser.AuditParser
import data.audit.datasource.csv.parser.CsvAuditParser
import data.audit.datasource.mongo.MongoAudit
import data.audit.mapper.AuditMapper
import data.audit.model.AuditDto
import data.audit.repository.AuditRepositoryImpl
import data.audit.repository.RemoteAuditDataSource
import data.authentication.datasource.csv.CsvUserParser
import data.authentication.datasource.inmemory.InMemoryLoggedUser
import data.authentication.datasource.mongo.MongoAuthentication
import data.authentication.mapper.UserMapper
import data.authentication.model.UserDto
import data.authentication.repository.AuthenticationRepositoryImpl
import data.authentication.repository.LoggedUserDataSource
import data.authentication.repository.RemoteAuthenticationDataSource
import data.project.datasource.csv.CsvProjectParser
import data.project.datasource.mongo.MongoProject
import data.project.mapper.ProjectMapper
import data.project.model.ProjectDto
import data.project.repository.ProjectRepositoryImpl
import data.project.repository.RemoteProjectDataSource
import data.task.datasource.csv.CsvTaskParser
import data.task.datasource.mongo.MongoTask
import data.task.mapper.TaskMapper
import data.task.model.TaskDto
import data.task.repository.RemoteTaskDataSource
import data.task.repository.TaskRepositoryImpl
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import logic.repository.ProjectRepository
import logic.repository.TaskRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single { ProjectMapper() }
    single { TaskMapper() }
    single { AuditMapper() }
    single { UserMapper() }
    single { CsvProjectParser() }
    single { CsvTaskParser(get()) }
    single<FileHelper> { CsvFileHelper() }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }
    single<RemoteTaskDataSource> { MongoTask(get(named("task"))) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AuditRepository> { AuditRepositoryImpl(get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<CsvUserParser> { CsvUserParser() }
    single<AuditParser> { CsvAuditParser(get()) }
    single<LoggedUserDataSource> { InMemoryLoggedUser() }
    single<RemoteAuthenticationDataSource> { MongoAuthentication(get(named("users"))) }
    single<RemoteProjectDataSource> { MongoProject(get(named("projects"))) }
    single<RemoteTaskDataSource> { MongoTask(get(named("tasks"))) }
    single<RemoteAuditDataSource> { MongoAudit(get(named("audits"))) }
    single {
        val client: MongoClient =
            MongoClient.create("mongodb+srv://abudhabi:DhXobvmTriIWl9y5@planmate.sj0lbdm.mongodb.net/?retryWrites=true&w=majority&appName=PlanMate")
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