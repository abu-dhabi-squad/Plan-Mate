package presentation

import di.appModule
import di.repositoryModule
import di.uiModule
import di.useCaseModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.auth.LoginByUserNameUseCaseUI
import kotlin.system.exitProcess

fun main() {
    startKoin {
        modules(appModule, repositoryModule, useCaseModule, uiModule)
    }
    runBlocking {
        getKoin().get<LoginByUserNameUseCaseUI>().launchUi()
    }
    exitProcess(0)
}