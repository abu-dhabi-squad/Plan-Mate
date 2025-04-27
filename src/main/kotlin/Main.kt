package squad.abudhabi

import org.koin.core.context.startKoin
import squad.abudhabi.di.appModule
import squad.abudhabi.di.repositoryModule
import squad.abudhabi.di.uiModule
import squad.abudhabi.di.useCaseModule

fun main() {
    startKoin {
        modules(appModule, repositoryModule, useCaseModule, uiModule)
    }
}