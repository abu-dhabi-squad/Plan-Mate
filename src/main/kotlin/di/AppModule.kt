package di

import data.authentication.datasource.CsvUserParser
import logic.validation.DateParser
import logic.validation.DateParserImpl
import org.koin.dsl.module
import data.project.datasource.CsvProjectDataSource
import data.project.datasource.CsvProjectParser
import data.project.datasource.ProjectDataSource
import logic.utils.Md5Hashing
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.validation.DateValidator
import logic.validation.DateValidatorImpl
import logic.validation.PasswordValidator
import logic.validation.StandardPasswordValidator
import logic.validation.TaskValidator
import logic.validation.TaskValidatorImpl
import presentation.ui_io.ConsolePrinter
import presentation.ui_io.ConsoleReader
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import logic.utils.HashingService
import logic.validation.DateTimeParser
import logic.validation.DateTimeParserImpl

val appModule = module {
    single<DateParser> { DateParserImpl() }
    single<DateValidator> { DateValidatorImpl(get()) }
    single<PasswordValidator> { StandardPasswordValidator() }
    single<TaskValidator> { TaskValidatorImpl() }
    single<HashingService> { Md5Hashing()  }
    single<InputReader> { ConsoleReader() }
    single<Printer> { ConsolePrinter() }
    single <CsvUserParser>{ CsvUserParser() }
    single<FileHelper> { CsvFileHelper() }
    single { CsvProjectParser() }
    single<ProjectDataSource> {
        CsvProjectDataSource(
            fileHelper = get(),
            csvProjectParser = get(),
            fileName = "projects.csv"
        )
    }

    single <DateTimeParser>{DateTimeParserImpl()  }
}