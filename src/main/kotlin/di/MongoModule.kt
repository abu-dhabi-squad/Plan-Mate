package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.audit.datasource.mongo.MongoAudit
import data.audit.model.AuditDto
import data.audit.repository.RemoteAuditDataSource
import data.authentication.datasource.mongo.MongoAuthentication
import data.authentication.model.UserDto
import data.authentication.repository.RemoteAuthenticationDataSource
import data.project.datasource.mongo.MongoProject
import data.project.model.ProjectDto
import data.project.repository.RemoteProjectDataSource
import data.task.datasource.mongo.MongoTask
import data.task.model.TaskDto
import data.task.repository.RemoteTaskDataSource
import data.utils.databaseprovider.MongoProvider.Companion.getMongoDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {
    // collections
    single(named("audits")) {
        val database: MongoDatabase = getMongoDatabase()
        database.getCollection<AuditDto>("audits")
    }
    single(named("projects")) {
        val database: MongoDatabase = getMongoDatabase()
        database.getCollection<ProjectDto>("projects")
    }
    single(named("tasks")) {
        val database: MongoDatabase = getMongoDatabase()
        database.getCollection<TaskDto>("tasks")
    }
    single(named("users")) {
        val database: MongoDatabase = getMongoDatabase()
        database.getCollection<UserDto>("users")
    }
    // mongo dataSource
    single<RemoteAuthenticationDataSource> { MongoAuthentication(get(named("users"))) }
    single<RemoteProjectDataSource> { MongoProject(get(named("projects"))) }
    single<RemoteTaskDataSource> { MongoTask(get(named("tasks"))) }
    single<RemoteAuditDataSource> { MongoAudit(get(named("audits"))) }

}

