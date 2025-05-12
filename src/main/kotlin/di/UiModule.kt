package di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.UIFeature
import presentation.audit.GetAuditForProjectUI
import presentation.audit.GetAuditForTaskUI
import presentation.auth.LoginByUserNameUI
import presentation.io.ConsolePrinter
import presentation.io.ConsoleReader
import presentation.io.InputReader
import presentation.io.Printer
import presentation.presentation.user.admin.ConsoleAdminMenuUI
import presentation.presentation.user.mate.ConsoleUserMenuUI
import presentation.presentation.user.usermanagement.CreateMateUserUseCaseUI
import presentation.presentation.utils.PromptService
import presentation.project.*
import presentation.taskmanagement.CreateTaskUI
import presentation.taskmanagement.DeleteTaskByIdUI
import presentation.taskmanagement.EditTaskUI
import presentation.taskmanagement.GetTasksByProjectIdUI

val uiModule = module {

    // I/O dependencies
    single<Printer> { ConsolePrinter() }
    single<InputReader> { ConsoleReader() }

    // UI components
    single { CreateProjectUI(get(), get(), get(), get(), get()) }
    single { EditProjectUI(get(), get(), get(), get()) }
    single { DeleteProjectUI(get(), get(), get(), get(), get(), get()) }
    single { CreateTaskUI(get(), get(), get(), get(), get(), get()) }
    single { EditTaskUI(get(), get(), get(), get(), get(), get(), get()) }
    single { DeleteTaskByIdUI(get(), get(), get(), get(), get(), get(), get()) }
    single { GetTasksByProjectIdUI(get(), get(), get(), get()) }
    single { CreateMateUserUseCaseUI(get(), get(), get()) }
    single { GetAuditForTaskUI(get(), get(), get(), get(), get()) }
    single { GetAuditForProjectUI(get(), get(), get(), get()) }
    single { AddStateToProjectUI(get(), get(), get(), get()) }
    single { EditStateOfProjectUI(get(), get(), get(), get()) }
    single { LoginByUserNameUI(get(), get(), get(), get(), get(), get()) }

    // Admin UIFeatures
    single<List<UIFeature>>(named("adminFeatures")) {
        listOf(
            UIFeature("Create Project", 1, get<CreateProjectUI>()),
            UIFeature("Edit Project", 2, get<EditProjectUI>()),
            UIFeature("Delete Project", 3, get<DeleteProjectUI>()),
            UIFeature("Add a TaskState to Project", 4, get<AddStateToProjectUI>()),
            UIFeature("Edit TaskState of Project", 5, get<EditStateOfProjectUI>()),
            UIFeature("Create Task", 6, get<CreateTaskUI>()),
            UIFeature("View Tasks by Project", 7, get<GetTasksByProjectIdUI>()),
            UIFeature("Create Mate User", 8, get<CreateMateUserUseCaseUI>()),
            UIFeature("Get Task Audit", 9, get<GetAuditForTaskUI>()),
            UIFeature("Get Project Audit", 10, get<GetAuditForProjectUI>()),
        )
    }

    // Mate UIFeatures
    single<List<UIFeature>>(named("mateFeatures")) {
        listOf(
            UIFeature("Create Task", 1, get<CreateTaskUI>()),
            UIFeature("Edit Task", 2, get<EditTaskUI>()),
            UIFeature("Delete Task", 3, get<DeleteTaskByIdUI>()),
            UIFeature("View Tasks by Project", 4, get<GetTasksByProjectIdUI>()),
            UIFeature("Get Task Audit", 5, get<GetAuditForTaskUI>())
        )
    }

    // Menu views
    single { ConsoleAdminMenuUI(get(named("adminFeatures")), get(), get()) }
    single { ConsoleUserMenuUI(get(named("mateFeatures")), get(), get()) }

    //PromptService
    single { PromptService(get(), get(), get()) }
}

