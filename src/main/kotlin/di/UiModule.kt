package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.project.EditProjectUI
import squad.abudhabi.presentation.project.EditStateOfProjectUI

val uiModule = module {
    single<UiLauncher> { EditProjectUI(get(), get(), get()) }
    single<UiLauncher> { EditStateOfProjectUI(get(), get(), get()) }
}