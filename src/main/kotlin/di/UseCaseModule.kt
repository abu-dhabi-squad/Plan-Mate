package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.logic.project.AddStateToProjectUseCase
import squad.abudhabi.logic.project.CreateProjectUseCase
import squad.abudhabi.logic.project.DeleteProjectUseCase

val useCaseModule = module {
    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }

}