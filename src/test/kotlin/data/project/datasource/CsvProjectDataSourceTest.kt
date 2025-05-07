package data.project.datasource

import com.google.common.truth.Truth
import data.project.datasource.csv_datasource.CsvProjectDataSource
import data.project.datasource.csv_datasource.CsvProjectParser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import data.utils.filehelper.FileHelper
import logic.model.Project
import logic.model.State
import java.util.*

class CsvProjectDataSourceTest {
    private lateinit var fileHelper: FileHelper
    private lateinit var csvProjectParser: CsvProjectParser
    private lateinit var csvProjectDataSource: CsvProjectDataSource

    @BeforeEach
    fun setup() {
        fileHelper = mockk(relaxed = true)
        csvProjectParser = mockk(relaxed = true)
        csvProjectDataSource = CsvProjectDataSource(fileHelper, csvProjectParser, "build/project.csv")
    }

    @Test
    fun `getAllProjects should throw Exception when read file throw Exception`() = runTest {
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getAllProjects()
        }
    }

    @Test
    fun `getAllProjects should returns empty list when read file returns empty list`()= runTest{
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        val res = csvProjectDataSource.getAllProjects()
        //then
        Truth.assertThat(res).isEqualTo(listOf<Project>())
    }

    @Test
    fun `getAllProjects should throw Exception when parser throw Exception`()= runTest {
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getAllProjects()
        }
    }

    @Test
    fun `getAllProjects should return list of projects when read file returns list`() = runTest{
        //given
        val resState = listOf(
            State("1", "state1"),
            State("2", "state2"),
            State("3", "state3")
        )
        val res = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resState))
        every { fileHelper.readFile(any()) } returns listOf("1,name1,1-state1|2-state2|3-state3")
        every { csvProjectParser.parseStringToProject(any()) } returns Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resState)
        // when
        val result = csvProjectDataSource.getAllProjects()
        // then
        Truth.assertThat(result).isEqualTo(res)
    }

    @Test
    fun `createProject should be success when there is no error occur`() = runTest{
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { csvProjectParser.parseProjectToString(any()) } returns "id1,name1,id1-name1"
        //when
        csvProjectDataSource.createProject(project)
        //then
        verify(exactly = 1) { fileHelper.appendFile("build/project.csv", listOf("id1,name1,id1-name1")) }
    }

    @Test
    fun `createProject should throw Exception when parser throw Exception`() = runTest{
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { csvProjectParser.parseProjectToString(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.createProject(project)
        }
    }

    @Test
    fun `createProject should throw Exception when append file throw Exception`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
        every { fileHelper.appendFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.createProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when readProjects throw Exception`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        coEvery { csvProjectDataSource.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when readFile throw Exception`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when parser throw Exception`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name2", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should write nothing when read projects returns empty list`()= runTest {
        //given
        val editProject = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        csvProjectDataSource.editProject(editProject)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf(",,")) }
    }

    @Test
    fun `editProject should throw Exception when write file throw Exception`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        coEvery { csvProjectDataSource.getAllProjects() } returns listOf(project)
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.editProject(project)
        }
    }

    @Test
    fun `editProject should edit when project in list`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val editProject = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name2", listOf())
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1", "id2,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        every { csvProjectParser.parseProjectToString(any()) } returns "id1,name1,id1-name1" andThen "id2,name2,"
        //when
        csvProjectDataSource.editProject(editProject)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf("id1,name1,id1-name1", "id2,name2,")) }
    }

    @Test
    fun `deleteProject should throw Exception when readProjects throw Exception`() = runTest{
        //given
        val projectId = "id1"
        coEvery { csvProjectDataSource.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should throw Exception when readFile throw Exception`()= runTest {
        //given
        val projectId = "id1"
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should throw Exception when parser throw Exception`()= runTest {
        //given
        val projectId = "id1"
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should write nothing when read projects returns empty list`()= runTest {
        //given
        val projectId = "id1"
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        csvProjectDataSource.deleteProject(projectId)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf(",,")) }
    }

    @Test
    fun `deleteProject should throw Exception when write file throw Exception`() = runTest{
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val projectId = "id1"
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.deleteProject(projectId)
        }
    }

   /* @Test
    fun `deleteProject should delete when id in list`() = runTest{
        //given
        val project1 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1", "id2,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project1 andThen project2
        every { csvProjectParser.parseProjectToString(project1) } returns "id1,name1,id1-name1"
        //when
        csvProjectDataSource.deleteProject(projectId.toString())
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf("id1,name1,id1-name1")) }
    }*/

    @Test
    fun `deleteProject should delete when delete the only project in list`() = runTest{
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { csvProjectParser.parseProjectToString(any()) } returns ",,"
        //when
        csvProjectDataSource.deleteProject(projectId.toString())
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf(",,")) }
    }

    @Test
    fun `getProjectById should throw Exception when readProjects throw Exception`()= runTest {
        //given
        coEvery { csvProjectDataSource.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProjectById("id1")
        }
    }

    @Test
    fun `getProjectById should return null when readProjects returns empty list`() = runTest{
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.getProjectById("id1")).isNull()
    }

    @Test
    fun `getProjectById should throw Exception when readFile throw Exception`() = runTest{
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProjectById("id1")
        }
    }

    @Test
    fun `getProjectById should throw Exception when parser throw Exception`() = runTest{
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProjectDataSource.getProjectById("id1")
        }
    }

    @Test
    fun `getProjectById should return null when the id not in the data`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        //when & then
        Truth.assertThat(csvProjectDataSource.getProjectById("id2")).isNull()
    }

    @Test
    fun `getProjectById should return the prject when the id in the data`()= runTest {
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("id1", "name1")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        //when & then
        Truth.assertThat(csvProjectDataSource.getProjectById(project.id.toString())).isEqualTo(project)
    }
}