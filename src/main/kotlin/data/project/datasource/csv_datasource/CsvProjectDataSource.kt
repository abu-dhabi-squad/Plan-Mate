package data.project.datasource.csv_datasource

import data.project.datasource.ProjectDataSource
import data.utils.filehelper.FileHelper
import logic.model.Project

class CsvProjectDataSource(
    private val fileHelper: FileHelper,
    private val csvProjectParser: CsvProjectParser,
    private val fileName: String
) : ProjectDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return fileHelper.readFile(fileName)
            .map(csvProjectParser::parseStringToProject)
    }

    override suspend fun createProject(project: Project) {
        appendProject(project)
    }

    override suspend fun editProject(project: Project) {
        val projects = getAllProjects()
        val newProjects = projects.map { currentProject -> currentProject.isEqualProject(project) }
        writeProjects(newProjects)
    }

    override suspend fun deleteProject(projectId: String) {
        val projects = getAllProjects()
        writeProjects(projects.filter { it.id.toString() != projectId })
    }

    override suspend fun getProjectById(projectId: String): Project? {
        return getAllProjects().find { it.id.toString() == projectId }
    }

    private fun writeProjects(projects: List<Project>) {
        if (projects.isEmpty()) fileHelper.writeFile(fileName, listOf(",,"))
        else fileHelper.writeFile(fileName, projects.map(csvProjectParser::parseProjectToString))
    }

    private fun appendProject(project: Project) {
        fileHelper.appendFile(fileName, listOf(csvProjectParser.parseProjectToString(project)))
    }

    private fun Project.isEqualProject(project: Project): Project {
        if (this.id == project.id) return project else return this
    }
}