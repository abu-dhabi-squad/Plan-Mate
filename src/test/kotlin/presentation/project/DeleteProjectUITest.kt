package presentation.project

import io.mockk.*
import logic.audit.CreateAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.project.DeleteProjectUseCase
import squad.abudhabi.presentation.project.DeleteProjectUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class DeleteProjectUITest{
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var deleteProjectUI: DeleteProjectUI
    private lateinit var createAuditUseCase: CreateAuditUseCase

    @BeforeEach
    fun setUp() {
        deleteProjectUseCase = mockk()
        createAuditUseCase = mockk(relaxed = true)
        inputReader = mockk()
        printer = mockk(relaxed = true)
        deleteProjectUI = DeleteProjectUI(deleteProjectUseCase, inputReader, printer, createAuditUseCase)
    }

    @Test
    fun `should print error message when project ID is blank`() {
        every { inputReader.readString() } returns ""

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should delete project and print success message when project ID is valid`() {
        val projectId = "PRJ-123"
        every { inputReader.readString() } returns projectId
        every { deleteProjectUseCase(projectId) } just Runs

        deleteProjectUI.launchUi()

        verify { deleteProjectUseCase(projectId) }
        verify { printer.displayLn("Project \"$projectId\" has been deleted.") }
    }

    @Test
    fun `should print error message when use case throws exception`() {
        val projectId = "PRJ-456"
        every { inputReader.readString() } returns projectId
        every { deleteProjectUseCase(projectId) } throws RuntimeException("Deletion failed")

        deleteProjectUI.launchUi()

        verify { printer.displayLn("Error: Deletion failed") }
    }

    @Test
    fun `should print error message when project ID is blank with spaces`() {
        every { inputReader.readString() } returns "   "

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should print error message when project ID is null`() {
        every { inputReader.readString() } returns null

        deleteProjectUI.launchUi()

        verify { printer.display("Enter the project ID to delete: ") }
        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { deleteProjectUseCase(any()) }
    }

    @Test
    fun `should throw exception when create audit throw exception`(){

        every { inputReader.readString() } returns "projectId"
        every { deleteProjectUseCase(any()) } just Runs
        every { createAuditUseCase(any()) } throws Exception()

        deleteProjectUI.launchUi()

        verify { printer.displayLn("Error: ${Exception().message}") }

    }
}