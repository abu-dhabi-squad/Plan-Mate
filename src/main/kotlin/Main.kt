import di.appModule
import di.repositoryModule
import di.uiModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.user.ConsoleUserMenuView


fun main() {
    startKoin {
        modules(appModule, repositoryModule, useCaseModule, uiModule)
    }
   // getKoin().get<ConsoleAdminMenuView>().launchUi()
    getKoin().get<ConsoleUserMenuView>().launchUi()

}