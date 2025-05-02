package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditProjectUseCase
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.presentation.project.EditProjectUI
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.Printer
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
    fun `launchUI should display Exception message when get all projects throw Exception`() {
        //given
        every { getAllProjectsUseCase() } throws Exception()
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn(Exception().message) }
    }

    @Test
    fun `launchUI should display there is no project in list when get all projects return empty list`() {
        //given
        every { getAllProjectsUseCase() } returns listOf()
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn("there is no project in list") }
    }

    @Test
    fun `launchUI should display list details when get all projects return list`() {
        //given
        val projects = listOf(
            Project("id1", "name1", listOf(State("id2", "name2"))),
            Project("id1", "name1", listOf(State("id2", "name2")))
        )
        every { getAllProjectsUseCase() } returns projects
        every { reader.readString() } returns null
        //when
        editProjectUI.launchUi()
        //then
        verify {
            projects.forEach{ project ->
                printer.displayLn(
                    "project id: " + project.id +
                            " - project name: " + project.projectName +
                            " - states : " + project.states
                )
            }

        }
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project id`() {
        //given
        every { getAllProjectsUseCase() } returns listOf(Project("id1","name1", listOf()))
        every { reader.readString() } returns null
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project name`() {
        //given
        every { getAllProjectsUseCase() } returns listOf(Project("id1","name1", listOf()))
        every { reader.readString() } returns "id1" andThen null
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display Exception message when edit project use case throw Exception`() {
        // Given
        every { getAllProjectsUseCase() } returns listOf(Project("id1","name1", listOf()))
        every { reader.readString() } returns "id1" andThen "name1"
        every { editProjectUseCase(any(), any()) } throws Exception()
        // When
        editProjectUI.launchUi()
        //then
        verify(exactly = 1) { editProjectUseCase(any(), any()) }
        verify { printer.displayLn(Exception().message) }
    }
}