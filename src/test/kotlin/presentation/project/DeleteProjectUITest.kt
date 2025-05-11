package presentation.project

import helper.createProject
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.exceptions.NoProjectsFoundException
import logic.project.DeleteProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.InputReader
import presentation.io.Printer


class DeleteProjectUITest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var reader: InputReader
    private lateinit var printer: Printer
    private lateinit var deleteProjectUI: DeleteProjectUI
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase

    @BeforeEach
    fun setUp() {
        deleteProjectUseCase = mockk()
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed = true)
        reader = mockk()
        printer = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        deleteProjectUI = DeleteProjectUI(
            deleteProjectUseCase,
            getAllProjectsUseCase,
            reader,
            printer,
            createAuditUseCase,
            getLoggedUserUseCase,
        )
    }

    @Test
    fun `should print exception message if getAllProjectsUseCase throws`() = runTest {
        val exception = NoProjectsFoundException()
        coEvery { getAllProjectsUseCase() } throws exception

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn(exception.message) }
    }

    @Test
    fun `should print exception message if createAuditUseCase throws`() = runTest {
        val exception = Exception("error")
        coEvery { getAllProjectsUseCase() } returns listOf(createProject())
        coEvery { createAuditUseCase(any()) } throws exception
        coEvery { reader.readInt() } returns 1
        coEvery { deleteProjectUseCase(any()) } just runs

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn(exception.message) }
    }

    @Test
    fun `should complete flow when user enter valid input and audit is succeeded`() = runTest {
        coEvery { getAllProjectsUseCase() } returns listOf(createProject(name = "test"))
        coEvery { reader.readInt() } returns 1
        coEvery { deleteProjectUseCase(any()) } just runs
        coEvery { createAuditUseCase(any()) } just runs

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn("\nProject \"test\" has been deleted.") }
    }

    @Test
    fun `should print message when no projects exist`() = runTest {
        coEvery { getAllProjectsUseCase() } returns emptyList()

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn("\nThere are no projects in the list.") }
    }

    @Test
    fun `should prompt again when input is null or blank`() = runTest {
        val projects = listOf(
            createProject(name = "project", taskStates = emptyList())
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returnsMany listOf(
            null, null, null, 1, // read project #
        )

        deleteProjectUI.launchUi()

        coVerify(exactly = 3) { printer.displayLn("\nInput cannot be empty.") }
        coVerify {
            deleteProjectUseCase(projects[0].projectId.toString())
        }
    }

    @Test
    fun `should not find project when input is out of projects range`() = runTest {
        val projects = listOf(
            createProject(name = "project", taskStates = emptyList())
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returns 5

        deleteProjectUI.launchUi()

        coVerify { printer.displayLn("\nProject not found") }
    }
}