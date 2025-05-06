package di

import data.authentication.datasource.csv_datasource.CsvUserParser
import data.database.MongoDataBaseProvider
import logic.validation.DateParser
import logic.validation.DateParserImpl
import org.koin.dsl.module
import data.project.datasource.csv_datasource.CsvProjectDataSource
import data.project.datasource.csv_datasource.CsvProjectParser
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
import org.koin.core.qualifier.named

val appModule = module {
    single { MongoDataBaseProvider("sayedmagdy", "mongodb://localhost:27017/") }

    single(named("projects")) { get<MongoDataBaseProvider>().getCollection("projects") }
    single(named("tasks")) { get<MongoDataBaseProvider>().getCollection("tasks") }
    single(named("audits")) { get<MongoDataBaseProvider>().getCollection("audits") }
    single(named("states")) { get<MongoDataBaseProvider>().getCollection("states") }
    single(named("users")) { get<MongoDataBaseProvider>().getCollection("users") }



    single<ProjectDataSource> {
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