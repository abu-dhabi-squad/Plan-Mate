package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.presentation.project.EditStateOfProjectUI
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.Printer
import kotlin.test.BeforeTest

class EditStateOfProjectUITest{

    private val printer: Printer = mockk(relaxed = true)
    private val reader: ConsoleReader = mockk(relaxed = true)
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase = mockk(relaxed = true)
    private lateinit var editStateOfProjectUI: EditStateOfProjectUI

    @BeforeTest
    fun setup() {
        editStateOfProjectUI = EditStateOfProjectUI(editStateOfProjectUseCase, reader, printer)
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project id`() {
        //given
        every { reader.readString() } returns null
        //when
        editStateOfProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for state id`() {
        //given
        every { reader.readString() } returns "id1" andThen null
        //when
        editStateOfProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for state name`() {
        //given
        every { reader.readString() } returns "id1" andThen "id1" andThen null
        //when
        editStateOfProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display error when edit project use case throw Exception`() {
        // Given
        every { reader.readString() } returns "id1" andThen "name1"
        every { editStateOfProjectUseCase(any(), any()) } throws Exception()
        // When
        editStateOfProjectUI.launchUi()
        //then
        verify(exactly = 1) { editStateOfProjectUseCase(any(), any()) }
        verify { printer.displayLn(Exception().message) }
    }
}