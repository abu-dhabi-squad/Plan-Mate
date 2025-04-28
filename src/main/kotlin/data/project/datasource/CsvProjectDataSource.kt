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

    override fun writeProjects(projects: List<Project>): Boolean {
        if (projects.isEmpty()) return false
        return fileHelper.writeFile(File(PROJECTS_FILE_NAME), projects.map(::buildStringFromProject))
    }

    private fun buildStringFromProject(project: Project): String {
        return project.id + "," +
                project.projectName + "," +
                project.states.map {
                    it.id + "-" + it.name + "|"
                }.dropLast(UNUSED_CHARACTER)
    }

    companion object {
        const val PROJECTS_FILE_NAME = "projects.csv"
        const val UNUSED_CHARACTER = 1
    }

}