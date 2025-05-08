package di

import data.authentication.datasource.csv_datasource.CsvUserParser
import logic.validation.DateParser
import logic.validation.DateParserImpl
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectDataSource
import data.project.datasource.csv_datasource.CsvProjectParser
import data.project.repository.LocalProjectDataSource
import logic.utils.Md5Hashing
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.validation.DateValidator
import logic.validation.DateValidatorImpl
import logic.validation.PasswordValidator
import logic.validation.StandardPasswordValidator
import logic.validation.TaskValidator
import logic.validation.TaskValidatorImpl
import presentation.io.ConsolePrinter
import presentation.io.ConsoleReader
import presentation.io.InputReader
import presentation.io.Printer
import logic.utils.HashingService
import logic.validation.DateTimeParser
import logic.validation.DateTimeParserImpl

val appModule = module {

    single<LocalProjectDataSource> {
        CsvProjectDataSource(
            fileHelper = get(),
            csvProjectParser = get(),
            fileName = "projects.csv"
        )
    }

    single<HashingService> { Md5Hashing() }

    single<InputReader> { ConsoleReader() }
    single<Printer> { ConsolePrinter() }

    single<FileHelper> { CsvFileHelper() }

    single<PasswordValidator> { StandardPasswordValidator() }
    single<DateValidator> { DateValidatorImpl(get()) }
    single<TaskValidator> { TaskValidatorImpl() }

    single<DateParser> { DateParserImpl() }
    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single<DateTimeParser> { DateTimeParserImpl() }
}