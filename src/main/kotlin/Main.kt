package presentation

import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.auth.LoginByUserNameUI
import di.*
import kotlin.system.exitProcess

fun main() {
    startKoin {
        properties(
            mapOf(
                "MONGO_URI" to System.getenv("MONGO_URI"),
                "MONGO_DB_NAME" to System.getenv("MONGO_DATABASE_NAME")
            )
        )
        modules(appModule, repositoryModule, useCaseModule, uiModule, mongoModule)
    }
    runBlocking {
        getKoin().get<LoginByUserNameUI>().launchUi()
    }
    exitProcess(0)
}