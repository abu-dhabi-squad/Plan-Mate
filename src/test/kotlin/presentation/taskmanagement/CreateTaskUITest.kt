package presentation.taskmanagement

import helper.createProject
import helper.createState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.validation.DateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.exceptions.InvalidTaskDateException
import logic.exceptions.NoProjectsFoundException
import logic.project.GetAllProjectsUseCase
import logic.task.CreateTaskUseCase
import presentation.io.InputReader
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase
import java.time.LocalDate

class CreateTaskUITest {

    private lateinit var printer: Printer
    private lateinit var reader: InputReader
    private lateinit var presenter: CreateTaskUI
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var parserDate: DateParser
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase : GetLoggedUserUseCase

    @BeforeEach
    fun setup() {
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        createTaskUseCase = mockk(relaxed = true)
        parserDate = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed =true)
        presenter = CreateTaskUI(
            getLoggedUserUseCase,
            printer,
            reader,
            getAllProjectsUseCase,
            createTaskUseCase,
            parserDate,
            createAuditUseCase
        )
    }


    @Test
    fun `should create a task when user input is valid`() = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "Title" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"
        coEvery { reader.readInt() } returns 1 andThen 1

        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)
        // When
        presenter.launchUi()
        // Then
        coVerify(exactly = 1) { createTaskUseCase(any()) }
    }

    @ParameterizedTest
    @CsvSource(",title", "    ,title", "null,title", nullValues = ["null"])
    fun `should display error message when user enter empty or null title`(
        firstEnterTitle: String?,
        secondEnterTitle: String
    ) = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns firstEnterTitle andThen secondEnterTitle andThen "Description" andThen "2025-10-10" andThen "2025-10-30"
        coEvery { reader.readInt() } returns 1 andThen 1

        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn(match { it == "\nInput cannot be empty." }) }
    }

    @ParameterizedTest
    @CsvSource(",description", "    ,description", "null,description", nullValues = ["null"])
    fun `should display error message when user enter empty or null description`(
        firstEnterDescription: String?,
        secondEnterDescription: String
    ) = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "title" andThen firstEnterDescription andThen secondEnterDescription andThen "2025-10-10" andThen "2025-10-30"
        coEvery { reader.readInt() } returns 1 andThen 1

        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)

        // When
        presenter.launchUi()

        // Then
        coVerify { printer.displayLn(match { it == "\nInput cannot be empty." }) }
    }


    @Test
    fun `should display message when no projects are available`() = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))
        coEvery { getAllProjectsUseCase() } returns emptyList() andThen listOf(fakeProject)
        coEvery { reader.readString() } returns "title" andThen "" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"

        //When
        presenter.launchUi()
        //Then
        coVerify { printer.displayLn(match { it == "\nNo projects available." }) }
    }

    @Test
    fun `should display error message when get all projects use case throw error`() = runTest{
        // Given
        coEvery { getAllProjectsUseCase() } throws NoProjectsFoundException()
        coEvery { reader.readString() } returns "title" andThen "" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"

        //When
        presenter.launchUi()
        //Then
        coVerify { printer.displayLn(match { it.toString().contains("No projects Found") }) }
    }

    @Test
    fun `should display error message when failed to create task`() = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "Title" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"
        coEvery { reader.readInt() } returns 1 andThen 1

        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)
        coEvery { createTaskUseCase(any()) } throws InvalidTaskDateException()
        //When
        presenter.launchUi()
        //Then
        coVerify { printer.displayLn(match { it.toString().contains("Invalid task date") }) }
    }

    @ParameterizedTest
    @CsvSource(",2025-10-10", "'    ',2025-10-10", "null,2025-10-10", nullValues = ["null"])
    fun `should display error when user enter empty or null date`(
        firstEnterDate: String?,
        secondEnterDate: String
    ) = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "Title" andThen "Description" andThen firstEnterDate andThen secondEnterDate andThen "2025-10-10" andThen "2025-10-30" andThen "2025-10-30"
        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)

        coEvery { reader.readInt() } returns 1 andThen 1

        // When
        presenter.launchUi()

        // Then
        coVerify { printer.displayLn(match { it.toString().contains("\nDate cannot be empty.") }) }
    }

    @Test
    fun `should display error when user enter date in invalid formatter`() = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "Title" andThen "Description" andThen "2025-10-10" andThen "2025-10-30" andThen "2025-10-30"
        coEvery { parserDate.parseDateFromString("2025-10-10") } throws Exception()
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)

        coEvery { reader.readInt() } returns 1 andThen 1

        // When
        presenter.launchUi()

        // Then
        coVerify {
            printer.displayLn(match {
                it.toString().contains("\nInvalid date format. Please use YYYY-MM-DD.")
            })
        }
    }

    @ParameterizedTest
    @CsvSource("2,1", "    ,1", "null,1", nullValues = ["null"])
    fun `should display error when user enter  invalid project or task number`(
        firstEnterIndex: Int?,
        secondEnterIndex: Int
    ) = runTest{
        // Given
        val fakeProject = createProject(states = listOf(createState()))
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        coEvery { reader.readString() } returns "Title" andThen "Description" andThen "2025-10-10" andThen "2025-10-30" andThen "2025-10-30"
        coEvery { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        coEvery { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)

        coEvery { reader.readInt() } returns firstEnterIndex andThen secondEnterIndex andThen 1

        // When
        presenter.launchUi()

        // Then
        coVerify {
            printer.displayLn(match {
                it.toString().contains("\nPlease enter a number between 1 and 1")
            })
        }
    }


}