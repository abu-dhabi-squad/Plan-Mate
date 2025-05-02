package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.presentation.project.GetProjectByIdUI

val uiModule = module {
 single{ GetProjectByIdUI(get(),get(),get()) }
}