package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.logic.project.GetProjectByIdUseCase

val useCaseModule = module {
    single { GetProjectByIdUseCase(get()) }
}