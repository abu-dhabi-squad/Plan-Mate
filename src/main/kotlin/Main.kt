package presentation

import di.appModule
import di.repositoryModule
import di.uiModule
import di.useCaseModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.auth.LoginByUserNameUI
import kotlin.system.exitProcess

fun main() {
    startKoin {
        modules(appModule, repositoryModule, useCaseModule, uiModule)
    }
    runBlocking {
        getKoin().get<LoginByUserNameUI>().launchUi()
    }
    exitProcess(0)
}