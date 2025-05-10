package di

import logic.audit.CreateAuditUseCase
import logic.audit.GetAuditUseCase
import logic.authentication.CreateMateUserUseCase
import logic.authentication.LoginByUserNameUseCase
import logic.authentication.validtion.CreateUserPasswordValidator
import logic.authentication.validtion.LoginPasswordValidator
import logic.project.AddStateToProjectUseCase
import logic.project.CreateProjectUseCase
import logic.project.DeleteProjectUseCase
import logic.project.EditProjectUseCase
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.project.GetProjectByIdUseCase
import logic.task.CreateTaskUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.EditTaskUseCase
import logic.task.GetTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import org.koin.dsl.module
import logic.user.GetLoggedUserUseCase
import logic.user.SaveLoggedUserUseCase

val useCaseModule = module {
    single { CreateAuditUseCase(get()) }
    single { GetAuditUseCase(get()) }

    single { CreateMateUserUseCase(get(), get<CreateUserPasswordValidator>()) }
    single { LoginByUserNameUseCase(get(),get<LoginPasswordValidator>()) }

    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { EditStateOfProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }

    single { CreateTaskUseCase(get(), get()) }
    single { DeleteTaskByIdUseCase(get()) }
    single { EditTaskUseCase(get(), get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }

    single { GetLoggedUserUseCase(get()) }
    single { SaveLoggedUserUseCase(get()) }
}