package data.project.datasource

import com.google.common.truth.Truth.assertThat
import data.project.datasource.csv.CsvProjectParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import logic.exceptions.CanNotParseProjectException
import logic.exceptions.CanNotParseStateException
import logic.model.Project
import logic.model.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state2|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;r;state3",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1|;state2|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state3",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state3",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state3",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,|",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;r;state1",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,state1",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;",
            "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,;state1",
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
        val resTaskStates = listOf(
            TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"),
            TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state2"),
            TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state3")
        )
        val line = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state2|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state3"
        //when & then
        assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resTaskStates))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with one state`() {
        //given
        val resTaskState = listOf(TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"))
        val line = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;state1"
        //when & then
        assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resTaskState))
    }

    @Test
    fun `parseStringToProject should return Project when the string could be splited with no state`() {
        //given
        val line = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,"
        //when & then
        assertThat(csvProjectParser.parseStringToProject(line)).isEqualTo(Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf()))
    }

    @Test
    fun`parseProjectToString should return a string when get a project with empty states`(){
        //given
        val project = Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf())
        //when & then
        assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with state`(){
        //given
        val project = Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf(TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1")))
        //when & then
        assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;name1")
    }

    @Test
    fun`parseProjectToString should return a string when get a project with states`(){
        //given
        val project = Project(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1", listOf(TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name1"),TaskState(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"name2")))
        //when & then
        assertThat(csvProjectParser.parseProjectToString(project)).isEqualTo("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,name1,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;name1|d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a;name2")
    }
}