package di

import logic.audit.CreateAuditUseCase
import logic.audit.GetAuditUseCase
import logic.authentication.CreateMateUserUseCase
import logic.authentication.LoginByUserNameUseCase
import logic.authentication.validtion.CreateUserPasswordValidator
import logic.authentication.validtion.LoginPasswordValidator
import logic.project.*
import logic.task.*
import logic.user.GetLoggedUserUseCase
import logic.user.SaveLoggedUserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { CreateAuditUseCase(get()) }
    single { GetAuditUseCase(get()) }

    single { CreateMateUserUseCase(get(), get<CreateUserPasswordValidator>(), get()) }
    single { LoginByUserNameUseCase(get(), get<LoginPasswordValidator>(), get()) }

    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get()) }
    single { EditStateOfProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }

    single { CreateTaskUseCase(get(), get()) }
    single { DeleteTaskByIdUseCase(get()) }
    single { EditTaskUseCase(get(), get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }

    single { GetLoggedUserUseCase(get()) }
    single { SaveLoggedUserUseCase(get()) }
}