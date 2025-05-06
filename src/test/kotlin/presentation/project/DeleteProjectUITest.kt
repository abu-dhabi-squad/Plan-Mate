package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
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
        every { inputReader.readString() } returns ""

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should delete project and print success message when project ID is valid`() = runTest{
        val projectId = "PRJ-123"
        every { inputReader.readString() } returns projectId
        every { deleteProjectUseCase(projectId) } just Runs

        deleteProjectUI.launchUi()

        verify { deleteProjectUseCase(projectId) }
        verify { printer.displayLn("Project \"$projectId\" has been deleted.") }
    }

    @Test
    fun `should print error message when use case throws exception`() = runTest{
        val projectId = "PRJ-456"
        every { inputReader.readString() } returns projectId
        every { deleteProjectUseCase(projectId) } throws RuntimeException("Deletion failed")

        deleteProjectUI.launchUi()

        verify { printer.displayLn("Error: Deletion failed") }
    }

    @Test
    fun `should print error message when project ID is blank with spaces`() = runTest{
        every { inputReader.readString() } returns "   "

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should print error message when project ID is null`() = runTest{
        every { inputReader.readString() } returns null

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should throw exception when create audit throw exception`() = runTest{

        every { inputReader.readString() } returns "projectId"
        every { deleteProjectUseCase(any()) } just Runs
        every { createAuditUseCase(any()) } throws Exception()

        deleteProjectUI.launchUi()

        verify { printer.displayLn("Error: ${Exception().message}") }

    }
}