package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.logic.repository.ProjectRepository

val repositoryModule = module {
    single<ProjectRepository> { ProjectRepositoryImpl(get ()) }
}