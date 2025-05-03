package di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.UIFeature
import presentation.admin.ConsoleAdminMenuView
import presentation.audit.GetAuditForProjectUI
import presentation.audit.GetAuditForTaskUI
import presentation.auth.LoginByUserNameUseCaseUI
import presentation.project.EditProjectUI
import presentation.project.EditStateOfProjectUI
import presentation.task_management.CreateTaskPresenterUI
import presentation.task_management.EditTaskPresenterUI
import presentation.task_management.GetTasksByProjectIdPresenterUI
import presentation.userManagement.CreateMateUserUseCaseUI
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import presentation.ui_io.ConsolePrinter
import presentation.ui_io.ConsoleReader
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import presentation.user.ConsoleUserMenuView
import squad.abudhabi.presentation.project.CreateProjectUI
import squad.abudhabi.presentation.project.DeleteProjectUI

val uiModule = module {

    // I/O dependencies
    single<Printer> { ConsolePrinter() }
    single<InputReader> { ConsoleReader() }

    // Shared resources
    single { User(username = "admin", password = "admin", userType = UserType.ADMIN) }

    // UI components
    single { CreateProjectUI(get(), get(), get(), get(), get()) }
    single { EditProjectUI(get(), get(), get(), get()) }
    single { DeleteProjectUI(get(), get(), get(), get(), get()) }
    single { CreateTaskPresenterUI(get(), get(), get(), get(), get(), get(), get()) }
    single { EditTaskPresenterUI(get(), get(), get(), get(), get(), get(), get(), get()) }
    single { GetTasksByProjectIdPresenterUI(get(), get(), get(), get()) }
    single { CreateMateUserUseCaseUI(get(), get(), get()) }
    single { GetAuditForTaskUI(get(), get(), get(), get(), get()) }
    single { GetAuditForProjectUI(get(), get(), get(), get()) }
    single { EditStateOfProjectUI(get(), get(), get(), get()) }
    single { LoginByUserNameUseCaseUI(get(), get(), get(), get(), get(), get()) }

    // Admin UIFeatures
    single<List<UIFeature>>(named("adminFeatures")) {
        listOf(
            UIFeature("Create Project", 1, get<CreateProjectUI>()),
            UIFeature("Edit Project", 2, get<EditProjectUI>()),
            UIFeature("Delete Project", 3, get<DeleteProjectUI>()),
            UIFeature("Create Task", 4, get<CreateTaskPresenterUI>()),
            UIFeature("View Tasks by Project", 5, get<GetTasksByProjectIdPresenterUI>()),
            UIFeature("Create Mate User", 6, get<CreateMateUserUseCaseUI>()),
            UIFeature("Get Task Audit", 7, get<GetAuditForTaskUI>()),
            UIFeature("Get Project Audit", 8, get<GetAuditForProjectUI>()),
            UIFeature("Edit State of Project", 9, get<EditStateOfProjectUI>())
        )
    }

    // Mate UIFeatures
    single<List<UIFeature>>(named("mateFeatures")) {
        listOf(
            UIFeature("Create Task", 1, get<CreateTaskPresenterUI>()),
            UIFeature("Edit Task", 2, get<EditTaskPresenterUI>()),
            UIFeature("Delete Project", 3, get<DeleteProjectUI>()),
            UIFeature("View Tasks by Project", 4, get<GetTasksByProjectIdPresenterUI>()),
            UIFeature("Get Task Audit", 5, get<GetAuditForTaskUI>())
        )
    }

    // Menu views
    single {
        ConsoleAdminMenuView(
            get(named("adminFeatures")), // List<UIFeature>
            get(), // Printer
            get()  // InputReader
        )
    }

    single {
        ConsoleUserMenuView(
            get(named("mateFeatures")), // List<UIFeature>
            get(), // Printer
            get()  // InputReader
        )
    }
}

