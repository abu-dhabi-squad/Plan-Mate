package di

import org.koin.dsl.module
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.project.datasource.CsvProjectParser
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.repository.ProjectRepository

val repositoryModule = module {
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<ProjectDataSource> { CsvProjectDataSource(get(), get(), "prject.csv") }
    single { CsvProjectParser() }
    single<FileHelper> { CsvFileHelper() }
}