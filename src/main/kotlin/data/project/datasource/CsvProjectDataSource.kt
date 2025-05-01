package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project

class CsvProjectDataSource(
    private val fileHelper: FileHelper,
    private val csvProjectParser: CsvProjectParser,
    private val fileName: String
) : ProjectDataSource {
    override fun getAllProjects(): List<Project> {
        return fileHelper.readFile(fileName)
            .map(csvProjectParser::parseStringToProject)
    }

    override fun createProject(project: Project) {
        val projects = getAllProjects()
        writeProjects(projects + project)
    }

    override fun editProject(project: Project) {
        val projects = getAllProjects()
        val newProjects = projects.map { currentProject -> currentProject.isEqualProject(project) }
        writeProjects(newProjects)
    }

    override fun deleteProject(projectId: String) {
        val projects = getAllProjects()
        writeProjects(projects.filter { it.id != projectId })
    }

    override fun getProject(projectId: String): Project? {
        return getAllProjects().toMutableList()
            .find { it.id == projectId }
    }

    private fun writeProjects(projects: List<Project>) {
        if (projects.isEmpty()) fileHelper.writeFile(fileName, listOf(",,"))
        else fileHelper.writeFile(fileName, projects.map(csvProjectParser::parseProjectToString))
    }

    private fun Project.isEqualProject(project: Project): Project {
        if (this.id == project.id) return project else return this
    }
}