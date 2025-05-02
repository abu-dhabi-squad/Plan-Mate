package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.presentation.ui_io.ConsolePrinter
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

val appModule = module {
    single<InputReader> { ConsoleReader() }
    single<Printer> { ConsolePrinter() }
}