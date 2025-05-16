package data.project.datasource

import com.google.common.truth.Truth.assertThat
import data.project.datasource.csv.CsvProject
import data.project.datasource.csv.CsvProjectParser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import data.utils.filehelper.FileHelper
import logic.model.Project
import logic.model.TaskState
import java.util.UUID

class CsvProjectTest {
    private lateinit var fileHelper: FileHelper
    private lateinit var csvProjectParser: CsvProjectParser
    private lateinit var csvProject: CsvProject

    @BeforeEach
    fun setup() {
        fileHelper = mockk(relaxed = true)
        csvProjectParser = mockk(relaxed = true)
        csvProject = CsvProject(fileHelper, csvProjectParser, "build/project.csv")
    }

    @Test
    fun `getAllProjects should throw Exception when read file throw Exception`(){
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.getAllProjects()
        }
    }

    @Test
    fun `getAllProjects should returns empty list when read file returns empty list`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        val res = csvProject.getAllProjects()
        //then
        assertThat(res).isEqualTo(listOf<Project>())
    }

    @Test
    fun `getAllProjects should throw Exception when parser throw Exception`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf("\"d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a\",name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.getAllProjects()
        }
    }

    @Test
    fun `getAllProjects should return list of projects when read file returns list`(){
        //given
        val resTaskStates = listOf(
            TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done"),
            TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done"),
            TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")
        )
        val res = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resTaskStates))
        every { fileHelper.readFile(any()) } returns listOf("1,name1,1-done|2-done|3-done")
        every { csvProjectParser.parseStringToProject(any()) } returns Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", resTaskStates)
        // when
        val result = csvProject.getAllProjects()
        // then
        assertThat(result).isEqualTo(res)
    }

    @Test
    fun `createProject should be success when there is no error occur`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { csvProjectParser.parseProjectToString(any()) } returns "id1,name1,id1-name1"
        //when
        csvProject.createProject(project)
        //then
        verify(exactly = 1) { fileHelper.appendFile("build/project.csv", listOf("id1,name1,id1-name1")) }
    }

    @Test
    fun `createProject should throw Exception when parser throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { csvProjectParser.parseProjectToString(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.createProject(project)
        }
    }

    @Test
    fun `createProject should throw Exception when append file throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
        every { fileHelper.appendFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.createProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when readProjects throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        coEvery { csvProject.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when readFile throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.editProject(project)
        }
    }

    @Test
    fun `editProject should throw Exception when parser throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name2", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.editProject(project)
        }
    }

    @Test
    fun `editProject should write nothing when read projects returns empty list`(){
        //given
        val editProject = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        csvProject.editProject(editProject)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf(",,")) }
    }

    @Test
    fun `editProject should throw Exception when write file throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        coEvery { csvProject.getAllProjects() } returns listOf(project)
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.editProject(project)
        }
    }

    @Test
    fun `editProject should not edit when project not in the list`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        val editProject = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name2", listOf())
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1", "id2,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        every { csvProjectParser.parseProjectToString(any()) } returns "id1,name1,id1-name1" andThen "id2,name2,"
        //when
        csvProject.editProject(editProject)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf("id1,name1,id1-name1", "id2,name2,")) }
    }

    @Test
    fun `editProject should edit when project in the list`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        val editProject = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name2", listOf())
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1", "id2,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        every { csvProjectParser.parseProjectToString(any()) } returns "id1,name1,id1-name1" andThen "id2,name2,"
        //when
        csvProject.editProject(editProject)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf("id1,name1,id1-name1", "id2,name2,")) }
    }

    @Test
    fun `deleteProject should throw Exception when readProjects throw Exception`(){
        //given
        val projectId = "any_id"
        coEvery { csvProject.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should throw Exception when readFile throw Exception`(){
        //given
        val projectId = "any_id"
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should throw Exception when parser throw Exception`(){
        //given
        val projectId = "any_id"
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.deleteProject(projectId)
        }
    }

    @Test
    fun `deleteProject should write nothing when read projects returns empty list`(){
        //given
        val projectId = "any_id"
        every { fileHelper.readFile(any()) } returns listOf()
        //when
        csvProject.deleteProject(projectId)
        //then
        verify(exactly = 1) { fileHelper.writeFile("build/project.csv", listOf(",,")) }
    }

    @Test
    fun `deleteProject should throw Exception when write file throw Exception`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        val projectId = "any_id"
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        every { fileHelper.writeFile(any(), any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.deleteProject(projectId)
        }
    }

   @Test
    fun `deleteProject should delete when id in list`(){
        //given
        val project1=Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
       val project2=Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { fileHelper.readFile(any()) } returns listOf("","")
       every { csvProjectParser.parseStringToProject(any()) } returns project1 andThen project2
       every{ csvProjectParser.parseProjectToString(project2)} returns "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b,name1"

        //when
       csvProject.deleteProject("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        //then
       verify { fileHelper.writeFile(any(), listOf("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b,name1")) }
    }

    @Test
    fun `getProjectById should throw Exception when readProjects throw Exception`(){
        //given
        coEvery { csvProject.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.getProjectById("any_id")
        }
    }

    @Test
    fun `getProjectById should return null when readProjects returns empty list`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        assertThat(csvProject.getProjectById("any_id")).isNull()
    }

    @Test
    fun `getProjectById should throw Exception when readFile throw Exception`(){
        //given
        every { fileHelper.readFile(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.getProjectById("any_id")
        }
    }

    @Test
    fun `getProjectById should throw Exception when parser throw Exception`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            csvProject.getProjectById("any_id")
        }
    }

    @Test
    fun `getProjectById should return null when the id not in the data`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "in_progress")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project
        //when & then
        assertThat(csvProject.getProjectById("any_id")).isNull()
    }

    @Test
    fun `getProjectById should return the prject when the id in the data`(){
        //given
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "in_progress")))
        val project2 = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "done")))
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } returns project andThen project2
        //when & then
        assertThat(csvProject.getProjectById(project.projectId.toString())).isEqualTo(project)
    }
}