package presentation.project

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.InputReader
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase


class DeleteProjectUITest{
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var deleteProjectUI: DeleteProjectUI
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase

    @BeforeEach
    fun setUp() {
        deleteProjectUseCase = mockk()
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed = true)
        inputReader = mockk()
        printer = mockk(relaxed = true)
        deleteProjectUI = DeleteProjectUI(deleteProjectUseCase, inputReader, printer, createAuditUseCase,getLoggedUserUseCase)
    }

    @Test
    fun `should print error message when project ID is blank`() = runTest{
        coEvery { inputReader.readString() } returns ""

        deleteProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to delete: ") }
        coVerify { printer.displayLn("Project name cannot be empty.") }
        coVerify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should delete project and print success message when project ID is valid`() = runTest{
        val projectId = "PRJ-123"
        coEvery { inputReader.readString() } returns projectId
        coEvery { deleteProjectUseCase(projectId) } just Runs

        deleteProjectUI.launchUi()

        coVerify { deleteProjectUseCase(projectId) }
        coVerify { printer.displayLn("Project \"$projectId\" has been deleted.") }
    }

    @Test
    fun `should print error message when use case throws exception`() = runTest{
        val projectId = "PRJ-456"
        coEvery { inputReader.readString() } returns projectId
        coEvery { deleteProjectUseCase(projectId) } throws RuntimeException("Deletion failed")

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn("Error: Deletion failed") }
    }

    @Test
    fun `should print error message when project ID is blank with spaces`() = runTest{
        coEvery { inputReader.readString() } returns "   "

        deleteProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to delete: ") }
        coVerify { printer.displayLn("Project name cannot be empty.") }
        coVerify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should print error message when project ID is null`() = runTest{
        coEvery { inputReader.readString() } returns null

        deleteProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to delete: ") }
        coVerify { printer.displayLn("Project name cannot be empty.") }
        coVerify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should throw exception when create audit throw exception`() = runTest{

        coEvery { inputReader.readString() } returns "projectId"
        coEvery { deleteProjectUseCase(any()) } just Runs
        coEvery { createAuditUseCase(any()) } throws Exception()

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn("Error: ${Exception().message}") }

    }
}