package di

import org.koin.dsl.module
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.project.datasource.CsvProjectParser
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.presentation.ui_io.ConsolePrinter
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

val appModule = module {
    single<InputReader> { ConsoleReader() }
    single<Printer> { ConsolePrinter() }
    single<FileHelper> { CsvFileHelper() }
    single { CsvProjectParser() }
    single<ProjectDataSource> {
        CsvProjectDataSource(
            fileHelper = get(),
            csvProjectParser = get(),
            fileName = "projects.csv"
        )
    }
}