package data.project.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.project.datasource.CsvProjectParser
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectDataSourceTest {
    private lateinit var fileHelper: FileHelper
    private lateinit var csvProjectParser: CsvProjectParser
    private lateinit var csvProjectDataSource: CsvProjectDataSource

    @BeforeEach
    fun setup() {
        fileHelper = mockk(relaxed = true)
        csvProjectParser = mockk(relaxed = true)
        csvProjectDataSource = CsvProjectDataSource(fileHelper, csvProjectParser)
    }

    @Test
    fun `readProjects should throw Exception when read file throw Exception`() {
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.readProjects()
        }
    }

    @Test
    fun `readProjects should returns empty list when read file returns empty list`() {
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.readProjects()).isEqualTo(listOf<Project>())
    }

    @Test
    fun `readProjects should throw Exception when parser throw Exception`() {
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.readProjects()
        }
    }

    @Test
    fun `readProjects should return list of projects when read file returns list`() {
        //given
        val resState = listOf(
            State("1", "state1"),
            State("2", "state2"),
            State("3", "state3")
        )
        val res = listOf(Project("1", "name1", resState))
        every { fileHelper.readFile(any()) } returns listOf("1,name1,1-state1|2-state2|3-state3")
        every { csvProjectParser.parseStringToProject(any()) } returns Project("1", "name1", resState)
        //when & then
        Truth.assertThat(csvProjectDataSource.readProjects()).isEqualTo(res)
    }

    @Test
    fun `writeProject should throw Exception when readProjects throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf())
        every { csvProjectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.writeProject(project)
        }
    }

    @Test
    fun `writeProject should throw Exception when readFile throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.writeProject(project)
        }
    }

    @Test
    fun `writeProject should return true when readProjects return empty list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf()
        every { fileHelper.writeFile(any(), any()) } returns true
        //when & then
        Truth.assertThat(csvProjectDataSource.writeProject(project)).isTrue()
    }

    @Test
    fun `writeProject should return true when readProjects return list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { fileHelper.writeFile(any(), any()) } returns true
        //when & then
        Truth.assertThat(csvProjectDataSource.writeProject(project)).isTrue()
    }

    @Test
    fun `writeProject should throw Exception when parser throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.writeProject(project)
        }
    }

    @Test
    fun `writeProject should throw Exception when write file throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf())
        every { csvProjectDataSource.readProjects() } returns listOf()
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.writeProject(project)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `writeProject should return like write file when read files return list`(returnedValue: Boolean) {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf()
        every { fileHelper.writeFile(any(), any()) } returns returnedValue
        //when & then
        Truth.assertThat(csvProjectDataSource.writeProject(project)).isEqualTo(returnedValue)
    }

    @Test
    fun `editProject should throw Exception when readProjects throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { csvProjectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when readFile throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when parser throw Exception`() {
        //given
        val project = Project("id1", "name2", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should return false when project not in list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        val editProject = Project("id2", "name2", listOf())
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        //when & then
        Truth.assertThat(csvProjectDataSource.editProject(editProject)).isFalse()
    }

    @Test
    fun `editProject should return false when read projects returns empty list`() {
        //given
        val editProject = Project("id2", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.editProject(editProject)).isFalse()
    }

    @Test
    fun `editProject should throw Exception when write file throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { csvProjectDataSource.readProjects() } returns listOf(project)
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `editProject should return like write file when project in data`(returnedValue: Boolean) {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } returns returnedValue
        //when & then
        Truth.assertThat(csvProjectDataSource.editProject(project)).isEqualTo(returnedValue)
    }

    @Test
    fun `editProject should return true when project in list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        val project2 = Project("id2", "name1", listOf(State("id1", "name1")))
        val editProject = Project("id2", "name2", listOf())
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1", "id2,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        every { fileHelper.writeFile(any(), any()) } returns true
        //when & then
        Truth.assertThat(csvProjectDataSource.editProject(editProject)).isTrue()
    }

    @Test
    fun `deleteProject should throw Exception when readProjects throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { csvProjectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(project)
        }
    }

    @Test
    fun `deleteProject should throw Exception when readFile throw Exception`() {
        //given
        val project = Project("id1", "name2", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(project)
        }
    }

    @Test
    fun `deleteProject should throw Exception when parser throw Exception`() {
        //given
        val project = Project("id1", "name2", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(project)
        }
    }

    @Test
    fun `deleteProject should return false when project not in list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        val editProject = Project("id2", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        //when & then
        Truth.assertThat(csvProjectDataSource.deleteProject(editProject)).isFalse()
    }

    @Test
    fun `deleteProject should return false when read projects returns empty list`() {
        //given
        val editProject = Project("id2", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.deleteProject(editProject)).isFalse()
    }

    @Test
    fun `deleteProject should throw Exception when write file throw Exception`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(project)
        }
    }

    @Test
    fun `deleteProject should return true when delete the only project in list`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } returns true
        //when & then
        Truth.assertThat(csvProjectDataSource.deleteProject(project)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `deleteProject should return like write file when project in data`(returnedValue: Boolean) {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } returns returnedValue
        //when & then
        Truth.assertThat(csvProjectDataSource.deleteProject(project))
    }


    @Test
    fun `getProject should throw Exception when readProjects throw Exception`() {
        //given
        every { csvProjectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProject("id1")
        }
    }

    @Test
    fun `getProject should return null when readProjects returns empty list`() {
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.getProject("id1")).isNull()
    }

    @Test
    fun `getProject should throw Exception when readFile throw Exception`() {
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProject("id1")
        }
    }

    @Test
    fun `getProject should throw Exception when parser throw Exception`() {
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProject("id1")
        }
    }

    @Test
    fun `getProject should return null when the id not in the data`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        //when & then
        Truth.assertThat(csvProjectDataSource.getProject("id2")).isNull()
    }

    @Test
    fun `getProject should return the prject when the id in the data`() {
        //given
        val project = Project("id1", "name1", listOf(State("id1", "name1")))
        val project2 = Project("id2", "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        //when & then
        Truth.assertThat(csvProjectDataSource.getProject("id1")).isEqualTo(project)
    }
}