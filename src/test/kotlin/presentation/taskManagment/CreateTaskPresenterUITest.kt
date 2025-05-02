package presentation.taskManagment

import helper.createProject
import helper.createState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.CreateTaskUseCase
import squad.abudhabi.logic.validation.DateParser
import squad.abudhabi.presentation.taskManagment.CreateTaskPresenterUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import java.time.LocalDate

class CreateTaskPresenterUITest {

    private val userName = "TestUser"
    private lateinit var printer: Printer
    private lateinit var readere: InputReader
    private lateinit var presenter: CreateTaskPresenterUI
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var parserDate: DateParser

    @BeforeEach
    fun setup() {
        readere = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        createTaskUseCase = mockk(relaxed = true)
        parserDate = mockk(relaxed = true)

        presenter = CreateTaskPresenterUI(
            userName,
            printer,
            readere,
            getAllProjectsUseCase,
            createTaskUseCase,
            parserDate
        )
    }


    @Test
    fun `should create a task when user input is valid`() {
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        every { getAllProjectsUseCase() } returns listOf(fakeProject)

        every { readere.readString() } returns "Title" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"
        every { readere.readInt() } returns 1 andThen 1

        every { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        every { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)
        // When
        presenter.launchUi()
        // Then
        verify(exactly = 1) { createTaskUseCase(any()) }
    }

    @ParameterizedTest
    @CsvSource(",title", "    ,title", "null,title", nullValues =["null"])
    fun `should display error message when user enter empty or null title`(
        firstEnterTitle: String?,
        secondEnterTitle: String
    ) {
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        every { getAllProjectsUseCase() } returns listOf(fakeProject)

        every { readere.readString() } returns firstEnterTitle andThen secondEnterTitle andThen "Description" andThen "2025-10-10" andThen "2025-10-30"
        every { readere.readInt() } returns 1 andThen 1

        every { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        every { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)
        // When
        presenter.launchUi()
        // Then
        verify { printer.display(match { it == "Input cannot be empty." }) }
    }

    @ParameterizedTest
    @CsvSource(",description", "    ,description", "null,description", nullValues = ["null"])
    fun `should display error message when user enter empty or null description`(
        firstEnterDescription: String?,
        secondEnterDescription: String
    ) {
        // Given
        val fakeProject = createProject(states = listOf(createState()))

        every { getAllProjectsUseCase() } returns listOf(fakeProject)

        every { readere.readString() } returns "title" andThen firstEnterDescription andThen secondEnterDescription andThen "2025-10-10" andThen "2025-10-30"
        every { readere.readInt() } returns 1 andThen 1

        every { parserDate.parseDateFromString("2025-10-10") } returns LocalDate.of(2025, 10, 10)
        every { parserDate.parseDateFromString("2025-10-30") } returns LocalDate.of(2025, 10, 30)

        // When
        presenter.launchUi()

        // Then
        verify { printer.display(match { it == "Input cannot be empty." }) }
    }


    @Test
    fun `should display message when no projects are available`() {
        // Given
        val fakeProject = createProject(states = listOf(createState()))
        every { getAllProjectsUseCase() } returns emptyList() andThen listOf(fakeProject)
        every { readere.readString() } returns "title" andThen "" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"

        //When
        presenter.launchUi()
        //Then
        verify { printer.display(match { it == "No projects available." }) }
    }

    @Test
    fun `should display error message when get all projects use case throw error`() {
        // Given
        every { getAllProjectsUseCase() } throws NoProjectsFoundException()
        every { readere.readString() } returns "title" andThen "" andThen "Description" andThen "2025-10-10" andThen "2025-10-30"

        //When
        presenter.launchUi()
        //Then
        verify { printer.display(match { it.toString().contains("No projects Found") }) }
    }

}