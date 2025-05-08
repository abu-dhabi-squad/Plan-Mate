package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import logic.model.Project
import logic.model.State
import logic.project.EditProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.io.ConsoleReader
import presentation.io.Printer
import java.util.*
import kotlin.test.BeforeTest

class EditProjectUITest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: ConsoleReader = mockk(relaxed = true)
    private val editProjectUseCase: EditProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private lateinit var editProjectUI: EditProjectUI

    @BeforeTest
    fun setup() {
        editProjectUI = EditProjectUI(editProjectUseCase, getAllProjectsUseCase, reader, printer)
    }

    @Test
    fun `launchUI should display Exception message when get all projects throw Exception`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //when
        editProjectUI.launchUi()
        //then
        coVerify { printer.displayLn(Exception().message) }
    }

    @Test
    fun `launchUI should display there is no project in list when get all projects return empty list`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } returns listOf()
        //when
        editProjectUI.launchUi()
        //then
        coVerify { printer.displayLn("there is no project in list") }
    }

    @Test
    fun `launchUI should display list details when get all projects return list`() = runTest{
        //given
        val projects = listOf(
            Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id2", "name2"))),
            Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id2", "name2")))
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readString() } returns null
        //when
        editProjectUI.launchUi()
        //then
        coVerify {
            projects.forEach{ project ->
                printer.displayLn(
                    "project id: " + project.id +
                            " - project name: " + project.projectName +
                            " - states : " + project.states
                )
            }

        }
        coVerify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project id`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } returns listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf()))
        coEvery { reader.readString() } returns null
        //when
        editProjectUI.launchUi()
        //then
        coVerify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project name`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } returns listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf()))
        coEvery { reader.readString() } returns "id1" andThen null
        //when
        editProjectUI.launchUi()
        //then
        coVerify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display Exception message when edit project use case throw Exception`() = runTest{
        // Given
        coEvery { getAllProjectsUseCase() } returns listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf()))
        coEvery { reader.readString() } returns "id1" andThen "name1"
        coEvery { editProjectUseCase(any(), any()) } throws Exception()
        // When
        editProjectUI.launchUi()
        //then
        coVerify(exactly = 1) { editProjectUseCase(any(), any()) }
        coVerify { printer.displayLn(Exception().message) }
    }
}
