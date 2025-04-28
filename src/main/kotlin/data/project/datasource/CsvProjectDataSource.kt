package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.Exceptions.CanNotParseProject
import squad.abudhabi.data.Exceptions.CanNotStateProject
import squad.abudhabi.data.Exceptions.NoProjectsFoundException
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import java.io.File


class CsvProjectDataSource(
    private val fileHelper: FileHelper
) : ProjectDataSource {
    override fun readProjects(): List<Project> {
        return fileHelper.readFile(File(PROJECTS_FILE_NAME))
            .takeIf { it.isNotEmpty() }
            ?.map(::parseStringToProject)
            ?: throw NoProjectsFoundException()
    }

    override fun writeProjects(projects: List<Project>): Boolean {
        if (projects.isEmpty()) throw NoProjectsFoundException()
        fileHelper.writeFile(File(PROJECTS_FILE_NAME), projects.map(::buildStringFromProject))
        return true
    }

    private fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf { it.isNotEmpty() && it.size == 3 }
            ?.let { projectRegx ->
                return Project(
                    projectRegx[ProjectColumnIndex.ID],
                    projectRegx[ProjectColumnIndex.NAME],
                    parseStringToListOfState(projectRegx[ProjectColumnIndex.STATES])
                )
            }
            ?: throw CanNotParseProject()
    }

    private fun buildStringFromProject(project: Project): String {
        return project.id + "," +
                project.projectName + "," +
                project.states.map {
                    it.id + "-" + it.name + "|"
                }.dropLast(1)
    }

    private fun parseStringToListOfState(subLine: String): List<State> {
        if (subLine.contains("|")) {
            val res: MutableList<State> = mutableListOf()
            subLine.split("|")
                .forEach { stateRegx ->
                    stateRegx.split("-")
                        .takeIf { it.isNotEmpty() && it.size == 2 }
                        ?.apply {
                            res.add(State(this[0], this[1]))
                        }
                        ?: throw CanNotStateProject()
                }
            return res
        } else {
            return subLine.split("-")
                .takeIf { it.isNotEmpty() && it.size == 2 }
                ?.map { State(it[0].toString(), it[1].toString()) }
                ?: throw CanNotStateProject()
        }
    }

    companion object {
        const val PROJECTS_FILE_NAME = "projects.csv"
    }
}