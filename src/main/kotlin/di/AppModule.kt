package di

import data.authentication.datasource.csv_datasource.CsvUserParser
import logic.validation.DateParser
import logic.validation.DateParserImpl
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectDataSource
import data.project.datasource.csv_datasource.CsvProjectParser
import data.project.repository.LocalProjectDataSource
import presentation.data.utils.hashing.Md5Hashing
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.validation.DateValidator
import logic.validation.DateValidatorImpl
import logic.authentication.validtion.CreateUserPasswordValidator
import logic.authentication.validtion.LoginPasswordValidator
import logic.validation.TaskValidator
import logic.validation.TaskValidatorImpl
import presentation.io.ConsolePrinter
import presentation.io.ConsoleReader
import presentation.io.InputReader
import presentation.io.Printer
import presentation.data.utils.hashing.HashingService
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

    single<CreateUserPasswordValidator> { CreateUserPasswordValidator() }
    single<LoginPasswordValidator> { LoginPasswordValidator() }

    single<DateValidator> { DateValidatorImpl(get()) }
    single<TaskValidator> { TaskValidatorImpl() }

    single<DateParser> { DateParserImpl() }
    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single<DateTimeParser> { DateTimeParserImpl() }
}