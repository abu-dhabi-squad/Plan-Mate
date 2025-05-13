package di

import data.audit.repository.AuditRepositoryImpl
import data.authentication.repository.AuthenticationRepositoryImpl
import data.project.repository.ProjectRepositoryImpl
import data.task.repository.TaskRepositoryImpl
import logic.repository.AuditRepository
import logic.repository.AuthenticationRepository
import logic.repository.ProjectRepository
import logic.repository.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AuditRepository> { AuditRepositoryImpl(get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
}