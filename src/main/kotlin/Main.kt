import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.auth.LoginByUserNameUI
import di.*
import kotlin.system.exitProcess

fun main() {
    startKoin {
        modules(appModule, repositoryModule, useCaseModule, uiModule, mongoModule)
    }
    runBlocking {
        getKoin().get<LoginByUserNameUI>().launchUi()
    }
    exitProcess(0)
}