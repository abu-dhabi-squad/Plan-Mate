package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.project.EditProjectUseCase
import squad.abudhabi.presentation.project.EditProjectUI
import squad.abudhabi.presentation.ui_io.ConsoleReader
import squad.abudhabi.presentation.ui_io.Printer
import kotlin.test.BeforeTest

class EditProjectUITest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: ConsoleReader = mockk(relaxed = true)
    private val editProjectUseCase: EditProjectUseCase = mockk(relaxed = true)
    private lateinit var editProjectUI: EditProjectUI

    @BeforeTest
    fun setup() {
        editProjectUI = EditProjectUI(editProjectUseCase, reader, printer)
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project id`() {
        //given
        every { reader.readString() } returns null
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display wrong input when enter wrong input or not entering at all for project name`() {
        //given
        every { reader.readString() } returns "id1" andThen null
        //when
        editProjectUI.launchUi()
        //then
        verify { printer.displayLn("wrong input") }
    }

    @Test
    fun `launchUI should display error when edit project use case throw Exception`() {
        // Given
        every { reader.readString() } returns "id1" andThen "name1"
        every { editProjectUseCase(any(), any()) } throws Exception()
        // When
        editProjectUI.launchUi()
        //then
        verify(exactly = 1) { editProjectUseCase(any(), any()) }
        verify { printer.displayLn(Exception().message) }
    }
}