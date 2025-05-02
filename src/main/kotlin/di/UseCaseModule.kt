package squad.abudhabi.di

import org.koin.dsl.module
import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.logic.project.EditProjectUseCase
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.logic.project.GetAllProjectsUseCase

val useCaseModule = module {
    single { GetProjectByIdUseCase(get()) }
    single { EditStateOfProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
}