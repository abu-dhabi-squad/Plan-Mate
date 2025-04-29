package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import squad.abudhabi.data.utils.filehelper.FileHelper

val appModule = module {
    single<FileHelper> {CsvFileHelper()}
}