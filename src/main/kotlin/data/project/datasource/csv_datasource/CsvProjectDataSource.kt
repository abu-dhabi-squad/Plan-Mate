package data.project.datasource.csv_datasource

import data.utils.filehelper.FileHelper
import logic.model.Project

class CsvProjectDataSource(
    private val fileHelper: FileHelper,
    private val csvProjectParser: CsvProjectParser,
    private val fileName: String
) : LocalProjectDataSource {
    override fun getAllProjects(): List<Project> {
        return fileHelper.readFile(fileName)
            .map(csvProjectParser::parseStringToProject)
    }

    override fun createProject(project: Project) {
        appendProject(project)
    }

    override fun editProject(project: Project) {
        val projects = getAllProjects()
        val newProjects = projects.map { currentProject -> currentProject.isEqualProject(project) }
        writeProjects(newProjects)
    }

    override fun deleteProject(projectId: String) {
        val projects = getAllProjects()
        writeProjects(projects.filter { it.id.toString() != projectId })
    }

    override fun getProjectById(projectId: String): Project? {
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
        return if (this.id == project.id) project else this
    }
}