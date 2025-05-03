package di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.UiLauncher
import presentation.admin.ConsoleAdminMenuView
import presentation.audit.GetAuditForProjectUI
import presentation.audit.GetAuditForTaskUI
import presentation.project.CreateProjectUI
import presentation.project.DeleteProjectUI
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

val uiModule = module {

    // I/O dependencies
    single<Printer> { ConsolePrinter() }
    single<InputReader> { ConsoleReader() }

    // Shared UI components
    single { User(username = "admin", password = "admin", userType = UserType.ADMIN) }
    single { CreateTaskPresenterUI("Noor Serry", get(), get(), get(), get(), get()) }
    single { GetTasksByProjectIdPresenterUI(get(), get(), get(), get()) }
    single { DeleteProjectUI(get(), get(), get()) }
    single { GetAuditForTaskUI(get(), get(), get(), get(), get()) }

    // Admin-specific UiLaunchers (individuals)
    single { CreateProjectUI(get(), get(), get()) }
    single { EditProjectUI(get(), get(), get(), get()) }
    single { CreateMateUserUseCaseUI(get(), get(), get()) }
    single { GetAuditForProjectUI(get(), get(), get(), get()) }
    single { EditStateOfProjectUI(get(), get(), get(), get()) }

    // Grouping admin UiLaunchers into a list manually
    single<List<UiLauncher>>(named("adminLaunchers")) {
        listOf(
            get<CreateProjectUI>(),
            get<EditProjectUI>(),
            get<DeleteProjectUI>(),
            get<CreateTaskPresenterUI>(),
            get<GetTasksByProjectIdPresenterUI>(),
            get<CreateMateUserUseCaseUI>(),
            get<GetAuditForTaskUI>(),
            get<GetAuditForProjectUI>(),
            get<EditStateOfProjectUI>()
        )
    }

    // Mate-specific UiLaunchers
    single<List<UiLauncher>>(named("mateLaunchers")) {
        listOf(
            get<CreateTaskPresenterUI>(),
            get<EditTaskPresenterUI>(),
            get<DeleteProjectUI>(),
            get<GetTasksByProjectIdPresenterUI>(),
            get<GetAuditForTaskUI>()
        )
    }

    // Mate UiLaunchers instances
    single { EditTaskPresenterUI(get(), get(), get(), get(), get()) }

    // Final ConsoleAdminMenuView with injected grouped list
    single {
        ConsoleAdminMenuView(
            get(), // User
            get(named("adminLaunchers")), // List<UiLauncher>
            get(), // Printer
            get()  // InputReader
        )
    }


    single {
        ConsoleUserMenuView(
            get(), // User
            get(named("mateLaunchers")), // List<UiLauncher>
            get(), // Printer
            get()  // InputReader
        )
    }


}
