package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.logic.repository.ProjectRepository

val repositoryModule = module {
    single<ProjectDataSource> { CsvProjectDataSource(get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
}