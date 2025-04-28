package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.Exceptions.CanNotParseProjectException
import squad.abudhabi.data.Exceptions.CanNotParseStateException
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
        return fileHelper.writeFile(File(PROJECTS_FILE_NAME), projects.map(::buildStringFromProject))
    }

    private fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf { it.isNotEmpty() && it.size == PROJECT_LINE_REGEX_NUMBER }
            ?.let { projectRegx ->
                return Project(
                    projectRegx[ProjectColumnIndex.ID],
                    projectRegx[ProjectColumnIndex.NAME],
                    parseStringToListOfState(projectRegx[ProjectColumnIndex.STATES])
                )
            }
            ?: throw CanNotParseProjectException()
    }


    private fun parseStringToListOfState(subLine: String): List<State> {
        if (subLine.contains("|")) {
            val res: MutableList<State> = mutableListOf()
            subLine.split("|")
                .forEach { stateRegx ->
                    stateRegx.split("-")
                        .takeIf { it.isNotEmpty() && it.size == STATE_LINE_REGEX_NUMBER }
                        ?.apply {
                            res.add(State(this[ProjectColumnIndex.STATE_ID], this[ProjectColumnIndex.STATE_NAME]))
                        }
                        ?: throw CanNotParseStateException()
                }
            return res
        } else {
            return subLine.split("-")
                .takeIf { it.isNotEmpty() && it.size == STATE_LINE_REGEX_NUMBER }
                ?.map { State(it[ProjectColumnIndex.STATE_ID].toString(), it[ProjectColumnIndex.STATE_NAME].toString()) }
                ?: throw CanNotParseStateException()
        }
    }


    private fun buildStringFromProject(project: Project): String {
        return project.id + "," +
                project.projectName + "," +
                project.states.map {
                    it.id + "-" + it.name + "|"
                }.dropLast(1)
    }

    companion object {
        const val PROJECTS_FILE_NAME = "projects.csv"
        const val PROJECT_LINE_REGEX_NUMBER = 3
        const val STATE_LINE_REGEX_NUMBER = 2
    }
}