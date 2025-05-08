//package data.project.repository
//
//import com.google.common.truth.Truth
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import data.project.datasource.ProjectDataSource
//import io.mockk.*
//import kotlinx.coroutines.test.runTest
//import logic.model.Project
//import logic.model.State
//import java.util.*
//
//class ProjectRepositoryImplTest {
//
//    private val projectDataSource: ProjectDataSource = mockk(relaxed = true)
//    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
//
//    @BeforeEach
//    fun setup() {
//        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource)
//    }
//
//    @Test
//    fun `getProjects should throw Exception when projectDataSource readProjects throw Exception`()= runTest {
//        //given
//        coEvery { projectDataSource.getAllProjects() } throws Exception()
//        //when & then
//        assertThrows<Exception> {
//            projectRepositoryImpl.getAllProjects()
//        }
//    }
//
//    @Test
//    fun `getProjects should return empty when projectDataSource readProjects return empty list`() = runTest {
//        //given
//        coEvery { projectDataSource.getAllProjects() } returns listOf()
//        //when & then
//        Truth.assertThat(projectRepositoryImpl.getAllProjects()).isEmpty()
//    }
//
//    @Test
//    fun `getProjects should return list of projects when projectDataSource readProjects return list of projects`()= runTest {
//        //given
//        val list = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf()))
//        coEvery { projectDataSource.getAllProjects() } returns list
//        //when & then
//        Truth.assertThat(projectRepositoryImpl.getAllProjects()).isEqualTo(list)
//    }
//
//    @Test
//    fun `addProject should throw Exception when projectDataSource writeProject throw Exception`()= runTest {
//        //given
//        val res = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
//        coEvery { projectDataSource.createProject(any()) } throws Exception()
//        //when & then
//        assertThrows<Exception> {
//            projectRepositoryImpl.addProject(res)
//        }
//    }
//
//    @Test
//    fun `addProject should be successful when no error occur (best case scenario)`()= runTest{
//        //given
//        val res = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
//        //when
//        projectRepositoryImpl.addProject(res)
//        //then
//        coVerify(exactly = 1) { projectDataSource.createProject(res) }
//    }
//
//    @Test
//    fun `editProject should throw Exception when projectDataSource editProject throw Exception`()= runTest {
//        //given
//        val res = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
//        coEvery { projectDataSource.editProject(any()) } throws Exception()
//        //when & then
//        assertThrows<Exception> {
//            projectRepositoryImpl.editProject(res)
//        }
//    }
//
//    @Test
//    fun `editProject should be successful when no error occur (best case scenario)`() = runTest{
//        //given
//        val res = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf())
//        //when
//        projectRepositoryImpl.editProject(res)
//        //then
//        coVerify(exactly = 1) { projectDataSource.editProject(res) }
//    }
//
//    @Test
//    fun `deleteProject should throw Exception when projectDataSource deleteProject throw Exception`() = runTest{
//        //given
//        val res = "1"
//        coEvery { projectDataSource.deleteProject(any()) } throws Exception()
//        //when & then
//        assertThrows<Exception> {
//            projectRepositoryImpl.deleteProject(res)
//        }
//    }
//
//    @Test
//    fun `deleteProject should be successful when no error occur (best case scenario)`() = runTest{
//        //given
//        val res = "1"
//        //when
//        projectRepositoryImpl.deleteProject(res)
//        //when & then
//        coVerify(exactly = 1) { projectDataSource.deleteProject(res) }
//    }
//
//    @Test
//    fun `getProjectById should throw Exception when projectDataSource getProject throw Exception`()= runTest {
//        //given
//        coEvery { projectDataSource.getProjectById(any()) } throws Exception()
//        //when & then
//        assertThrows<Exception> {
//            projectRepositoryImpl.getProjectById("1")
//        }
//    }
//
//    @Test
//    fun `getProjectById should return null when projectDataSource getProject return null`() = runTest{
//        //given
//        coEvery { projectDataSource.getProjectById(any()) } returns null
//        //when & then
//        Truth.assertThat(projectRepositoryImpl.getProjectById("1")).isNull()
//    }
//
//    @Test
//    fun `getProjectById should return project when projectDataSource getProject return project`() = runTest{
//        //given
//        val res = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("1", "name1")))
//        coEvery { projectDataSource.getProjectById(any()) } returns res
//        //when & then
//        Truth.assertThat(projectRepositoryImpl.getProjectById("1")).isEqualTo(res)
//    }
//}