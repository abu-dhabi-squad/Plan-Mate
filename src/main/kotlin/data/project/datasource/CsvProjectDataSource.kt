package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project

class CsvProjectDataSource(
    private val fileHelper: FileHelper,
    private val csvProjectParser: CsvProjectParser,
    private val fileName: String
) : ProjectDataSource {
    override fun readProjects(): List<Project> {
        return fileHelper.readFile(fileName)
            .map(csvProjectParser::parseStringToProject)
    }

    override fun writeProject(project: Project) {
        val projects = readProjects()
        writeProjects(projects + project)
    }

    override fun editProject(project: Project) {
        val projects = readProjects().toMutableList()
        projects.find { it.id == project.id } ?: throw ProjectNotFoundException(project.id)
        val newProjects = projects.map { currentProject -> currentProject.isEqualProject(project) }
        writeProjects(newProjects)
    }

    override fun deleteProject(projectId: String) {
        val projects = readProjects().toMutableList()
        projects.find { it.id == projectId } ?: throw ProjectNotFoundException(projectId)
        writeProjects(projects.filter { it.id != projectId })
    }

    override fun getProject(projectId: String): Project? {
        return readProjects().toMutableList()
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