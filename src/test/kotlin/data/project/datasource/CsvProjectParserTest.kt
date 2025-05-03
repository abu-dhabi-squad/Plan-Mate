package data.project.datasource

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.logic.exceptions.CanNotParseProjectException
import squad.abudhabi.logic.exceptions.CanNotParseStateException
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
            "1,name1,ee,1-state1|2-state2|3-state3",
            "1,name1",
            "1,",
            "",
            "1",
            ",name1,1-state1",
            "1,,1-state1",
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
            "id1,name1,1-state1|2-state2|3-r-state3",
            "id1,name1,1-state1|-state2|3-state3",
            "id1,name1,1-state1|2-|3-state3",
            "id1,name1,1-state1|2|3-state3",
            "id1,name1,|",
            "id1,name1,1-r-state1",
            "id1,name1,state1",
            "id1,name1,1-",
            "id1,name1,-state1",
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
            State("1", "state1"),
            State("2", "state2"),
            State("3", "state3")
        )
        val line = "1,name1,1-state1|2-state2|3-state3"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("1", "name1", resState))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with one state`() {
        //given
        val resState = listOf(State("1", "state1"))
        val line = "1,name1,1-state1"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("1", "name1", resState))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with no state`() {
        //given
        val line = "1,name1,"
        //when & then
        Truth.assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project("1", "name1", listOf()))
    }

    @Test
    fun`parseProjectToString should return a string when get a project with empty states`(){
        //given
        val project = Project("id1","name1", listOf())
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("id1,name1,")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with state`(){
        //given
        val project = Project("id1","name1", listOf(State("id1","name1")))
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("id1,name1,id1-name1")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with states`(){
        //given
        val project = Project("id1","name1", listOf(State("id1","name1"),State("id2","name2")))
        //when & then
        Truth.assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("id1,name1,id1-name1|id2-name2")
    }
}