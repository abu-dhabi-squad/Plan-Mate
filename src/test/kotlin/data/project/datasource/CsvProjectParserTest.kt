package data.project.datasource

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.logic.exceptions.CanNotParseProjectException
import squad.abudhabi.logic.exceptions.CanNotParseStateException
import squad.abudhabi.data.project.datasource.CsvProjectParser
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectParserTest {

    private lateinit var csvProjectParser: CsvProjectParser

    @BeforeEach
    fun setup() {
        csvProjectParser = CsvProjectParser()
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "f1925419-22c9-48f5-9e5b,name1,ee,f1925419-22c9-48f5-9e5b;state1|f1925419-22c9-48f5-9e5b;state2|f1925419-22c9-48f5-9e5b;state3",
            "f1925419-22c9-48f5-9e5b,name1",
            "f1925419-22c9-48f5-9e5b,",
            "",
            "1",
            ",name1,f1925419-22c9-48f5-9e5b;state1",
            "f1925419-22c9-48f5-9e5b,,f1925419-22c9-48f5-9e5b;state1",
        ]
    )
    fun `parseStringToProject should throw CanNotParseProjectException when the string split with not equal to 3 regex`(
        line: String
    ) {
        //given
        //when & then
        assertThrows<CanNotParseProjectException> {
            csvProjectParser.parseStringToProject(line)
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1|f1925419-22c9-48f5-9e5b;state2|f1925419-22c9-48f5-9e5b;r;state3",
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1|;state2|f1925419-22c9-48f5-9e5b;state3",
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1|f1925419-22c9-48f5-9e5b;|f1925419-22c9-48f5-9e5b;state3",
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1|f1925419-22c9-48f5-9e5b|f1925419-22c9-48f5-9e5b;state3",
            "f1925419-22c9-48f5-9e5b,name1,|",
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;r;state1",
            "f1925419-22c9-48f5-9e5b,name1,state1",
            "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;",
            "f1925419-22c9-48f5-9e5b,name1,;state1",
        ]
    )
    fun `parseStringToProject should throw CanNotParseStateException when the string is not valid`(input: String) {
        //given
        //when & then
        assertThrows<CanNotParseStateException> {
            csvProjectParser.parseStringToProject(input)
        }
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited`() {
        //given
        val resState = listOf(
            State("f1925419-22c9-48f5-9e5b", "state1"),
            State("f1925419-22c9-48f5-9e5b", "state2"),
            State("f1925419-22c9-48f5-9e5b", "state3")
        )
        val line = "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1|f1925419-22c9-48f5-9e5b;state2|f1925419-22c9-48f5-9e5b;state3"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("f1925419-22c9-48f5-9e5b", "name1", resState))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with one state`() {
        //given
        val resState = listOf(State("f1925419-22c9-48f5-9e5b", "state1"))
        val line = "f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;state1"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("f1925419-22c9-48f5-9e5b", "name1", resState))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with no state`() {
        //given
        val line = "f1925419-22c9-48f5-9e5b,name1,"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("f1925419-22c9-48f5-9e5b", "name1", listOf()))
    }

    @Test
    fun`parseProjectToString should return a string when get a project with empty states`(){
        //given
        val project = Project("f1925419-22c9-48f5-9e5b","name1", listOf())
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("f1925419-22c9-48f5-9e5b,name1,")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with state`(){
        //given
        val project = Project("f1925419-22c9-48f5-9e5b","name1", listOf(State("f1925419-22c9-48f5-9e5b","name1")))
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;name1")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with states`(){
        //given
        val project = Project("f1925419-22c9-48f5-9e5b","name1", listOf(State("f1925419-22c9-48f5-9e5b","name1"),State("f1925419-22c9-48f5-9e5b","name2")))
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("f1925419-22c9-48f5-9e5b,name1,f1925419-22c9-48f5-9e5b;name1|f1925419-22c9-48f5-9e5b;name2")
    }
}