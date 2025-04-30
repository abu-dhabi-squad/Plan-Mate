package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import java.io.File

class CsvProjectDataSource(
    private val fileHelper: FileHelper,
    private val csvProjectParser: CsvProjectParser
) : ProjectDataSource {
    override fun readProjects(): List<Project> {
        return fileHelper.readFile(File(PROJECTS_FILE_NAME))
            .map(csvProjectParser::parseStringToProject)
    }

    override fun writeProject(project: Project): Boolean {
        val projects = readProjects().toMutableList()
        projects.add(project)
        return writeProjects(projects)
    }

    override fun editProject(project: Project): Boolean {
        val projects = readProjects().toMutableList()
        projects.find { it.id == project.id } ?: return false
        projects.map { currentProject -> currentProject.isEqualProject(project) }
        return writeProjects(projects)
    }

    override fun deleteProject(projectId: String): Boolean {
        val projects = readProjects().toMutableList()
        projects.find { it.id == projectId } ?: return false
        projects.filter { it.id != projectId }
        return writeProjects(projects)
    }

    override fun getProject(projectId: String): Project? {
        return readProjects().toMutableList()
            .find { it.id == projectId }
    }

    private fun writeProjects(projects: List<Project>): Boolean {
        return fileHelper.writeFile(File(PROJECTS_FILE_NAME), projects.map(::buildStringFromProject))
    }

    private fun buildStringFromProject(project: Project): String {
        var res = project.id + "," + project.projectName + ","
        if (project.states.isEmpty()) return res
        project.states.forEach {
            res += it.id + "-" + it.name + "|"
        }
        return res.dropLast(UNUSED_CHARACTER)
    }

    private fun Project.isEqualProject(project: Project): Project {
        if (this.id == project.id) return project else return this
    }

    companion object {
        const val PROJECTS_FILE_NAME = "projects.csv"
        const val UNUSED_CHARACTER = 1
    }

}