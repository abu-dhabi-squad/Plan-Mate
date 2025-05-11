package di

import data.authentication.datasource.csv.CsvUserParser
import data.project.datasource.csv.CsvProject
import data.project.datasource.csv.CsvProjectParser
import data.project.repository.LocalProjectDataSource
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.authentication.validtion.CreateUserPasswordValidator
import logic.authentication.validtion.LoginPasswordValidator
import org.koin.dsl.module
import presentation.data.utils.hashing.Md5Hashing
import presentation.io.ConsolePrinter
import presentation.io.ConsoleReader
import presentation.io.InputReader
import presentation.io.Printer
import presentation.logic.task.validation.TaskValidator
import presentation.logic.task.validation.TaskValidatorImpl
import presentation.logic.utils.DateParser
import presentation.logic.utils.DateParserImpl
import presentation.logic.utils.DateTimeParser
import presentation.logic.utils.DateTimeParserImpl
import presentation.logic.utils.hashing.HashingService

val appModule = module {

    single<LocalProjectDataSource> {
        CsvProject(
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

    single<TaskValidator> { TaskValidatorImpl() }

    single<DateParser> { DateParserImpl() }
    single { CsvProjectParser() }
    single<CsvUserParser> { CsvUserParser() }
    single<DateTimeParser> { DateTimeParserImpl() }
}