package di

import data.audit.datasource.csv.parser.CsvAuditParser
import data.audit.mapper.AuditMapper
import data.authentication.datasource.csv.CsvUserParser
import data.authentication.datasource.inmemory.InMemoryLoggedUser
import data.authentication.mapper.UserMapper
import data.authentication.repository.LoggedUserDataSource
import data.project.datasource.csv.CsvProject
import data.project.datasource.csv.CsvProjectParser
import data.project.mapper.ProjectMapper
import data.project.repository.LocalProjectDataSource
import data.task.datasource.csv.CsvTaskParser
import data.task.mapper.TaskMapper
import data.utils.filehelper.CsvFileHelper
import data.utils.filehelper.FileHelper
import logic.authentication.validation.CreateUserPasswordValidator
import logic.authentication.validation.LoginPasswordValidator
import org.koin.dsl.module
import data.utils.hashing.Md5Hashing
import presentation.io.ConsolePrinter
import presentation.io.ConsoleReader
import presentation.io.InputReader
import presentation.io.Printer
import logic.task.validation.TaskValidator
import logic.task.validation.TaskValidatorImpl
import logic.utils.DateParser
import logic.utils.DateParserImpl
import logic.utils.DateTimeParser
import logic.utils.DateTimeParserImpl
import logic.utils.hashing.HashingService

val appModule = module {
    // Services
    single<HashingService> { Md5Hashing() }
    // IO
    single<InputReader> { ConsoleReader() }
    single<Printer> { ConsolePrinter() }
    // Helpers
    single<FileHelper> { CsvFileHelper() }
    // Validators
    single<CreateUserPasswordValidator> { CreateUserPasswordValidator() }
    single<LoginPasswordValidator> { LoginPasswordValidator() }
    single<TaskValidator> { TaskValidatorImpl() }
    // Parsers
    single<DateParser> { DateParserImpl() }
    single<CsvUserParser> { CsvUserParser() }
    single<DateTimeParser> { DateTimeParserImpl() }
    single { CsvAuditParser(get()) }
    single { CsvProjectParser() }
    single { CsvTaskParser(get()) }
    // Mappers
    single { ProjectMapper() }
    single { TaskMapper() }
    single { AuditMapper() }
    single { UserMapper() }
    // DataSources
    single<LocalProjectDataSource> {
        CsvProject(
            fileHelper = get(), csvProjectParser = get(), fileName = "projects.csv"
        )
    }
    single<LoggedUserDataSource> { InMemoryLoggedUser() }
}