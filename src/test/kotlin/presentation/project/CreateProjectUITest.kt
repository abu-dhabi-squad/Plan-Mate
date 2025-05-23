package presentation.project

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase
import presentation.utils.PromptUtils

class CreateProjectUITest {
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val promptUtils: PromptUtils = mockk(relaxed = true)
    private val createAuditUseCase: CreateAuditUseCase = mockk(relaxed = true)
    private val getLoggedUserUseCase: GetLoggedUserUseCase = mockk(relaxed = true)
    private lateinit var ui: CreateProjectUI

    @BeforeEach
    fun setup() {
        ui = CreateProjectUI(
            createProjectUseCase = createProjectUseCase,
            printer = printer,
            promptUtils = promptUtils,
            createAuditUseCase = createAuditUseCase,
            getLoggedUserUseCase = getLoggedUserUseCase
        )
    }

    @Test
    fun `launchUi should create project successfully when entering number more than 0 as number of states`() = runTest {
        //Given
        every { promptUtils.promptNonEmptyString(any()) } returns "projectName" andThen "stateName"
        every { promptUtils.promptNonEmptyInt(any()) } returns 1
        //When
        ui.launchUi()
        //Then
        coVerify { createProjectUseCase(any()) }
        coVerify { createAuditUseCase(any()) }
        verify { printer.displayLn(match { it.toString().contains("created") }) }
    }

    @Test
    fun `launchUi should create project successfully when entering 0 as number of states`() = runTest {
        //Given
        every { promptUtils.promptNonEmptyString(any()) } returns "projectName"
        every { promptUtils.promptNonEmptyInt(any()) } returns 0
        //When
        ui.launchUi()
        //Then
        coVerify { createProjectUseCase(any()) }
        coVerify { createAuditUseCase(any()) }
        verify { printer.displayLn(match { it.toString().contains("created") }) }
    }

    @Test
    fun `launchUi should show Invalid number message when entering number less than 0 as number of states`() = runTest {
        //Given
        every { promptUtils.promptNonEmptyString(any()) } returns "projectName"
        every { promptUtils.promptNonEmptyInt(any()) } returns -1
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("Invalid number") }) }
    }

    @Test
    fun `launchUi should show error message when createProjectUseCase throw exception`() = runTest {
        //Given
        every { promptUtils.promptNonEmptyString(any()) } returns "projectName" andThen "stateName"
        every { promptUtils.promptNonEmptyInt(any()) } returns 1
        coEvery { createProjectUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should show error message when createAuditUseCase throw exception`() = runTest {
        //Given
        every { promptUtils.promptNonEmptyString(any()) } returns "projectName" andThen "stateName"
        every { promptUtils.promptNonEmptyInt(any()) } returns 1
        coEvery { createAuditUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}