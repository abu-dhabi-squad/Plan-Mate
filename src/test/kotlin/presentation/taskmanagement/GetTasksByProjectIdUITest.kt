package presentation.taskmanagement

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.taskmanagement.TestData.fakeProject
import presentation.taskmanagement.TestData.fakeTask

class GetTasksByProjectIdUITest {
    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var presenter: GetTasksByProjectIdUI

    @BeforeEach
    fun setUp() {
        printer = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        getAllProjectsUseCase = mockk()
        getTasksByProjectIdUseCase = mockk()

        presenter = GetTasksByProjectIdUI(
            printer,
            promptService,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase
        )
    }

    @Test
    fun `should show tasks for selected project when everything is valid`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns listOf(fakeTask)
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn(match { it.toString().contains("=== Available Projects") }) }
        verify { printer.displayLn(match { it.toString().contains(fakeTask.title) }) }
    }

    @Test
    fun `should display error when loading projects fails`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } throws RuntimeException("DB error")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nFailed to load projects: DB error") }
    }

    @Test
    fun `should display message when no projects are available`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nNo projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } throws RuntimeException("Network issue")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nFailed to load tasks: Network issue") }
    }

    @Test
    fun `should display message when no tasks found for selected project`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nNo tasks found in '${fakeProject.projectName}'.") }
    }

    @ParameterizedTest
    @CsvSource("2,1", "null,1", nullValues = ["null"])
    fun `should prompt again if invalid project number is entered`(
        firstAttempt: Int?,
        secondAttempt: Int
    ) = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(
            firstAttempt ?: -1,
            secondAttempt - 1
        )
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns emptyList()

        // When
        presenter.launchUi()

        // Then
        verify { printer.displayLn("Please enter a number between")}
        verify { printer.displayLn("\nNo tasks found in '${fakeProject.projectName}'.") }
    }
}
