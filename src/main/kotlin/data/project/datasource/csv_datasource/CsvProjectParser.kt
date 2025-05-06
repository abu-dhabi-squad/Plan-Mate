package data.project.datasource.csv_datasource

import squad.abudhabi.data.project.datasource.ProjectColumnIndex
import squad.abudhabi.logic.exceptions.CanNotParseProjectException
import squad.abudhabi.logic.exceptions.CanNotParseStateException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import java.util.UUID

class CsvProjectParser {

    fun parseProjectToString(project: Project): String {
        var res = project.id.toString() + "," + project.projectName + ","
        if (project.states.isEmpty()) return res
        project.states.forEach {
            res += it.id + ";" + it.name + "|"
        }
        return res.dropLast(UNUSED_CHARACTER)
    }

    fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf(::isValidProject)
            ?.let { projectRegex ->
                return Project(
                    UUID.fromString(projectRegex[ProjectColumnIndex.ID]),
                    projectRegex[ProjectColumnIndex.NAME],
                    parseStringToListOfState(projectRegex[ProjectColumnIndex.STATES])
                )
            }
            ?: throw CanNotParseProjectException()
    }

    //ID,NAME,STATE   -->       ID/NAME|ID/NAME
    private fun parseStringToListOfState(subLine: String): List<State> {
        return if (subLine.contains("|")) {
            parseMultipleStates(subLine)
        } else if (subLine.contains(";")) {
            parseOneState(subLine)
        } else {
            if (subLine.isNotEmpty()) throw CanNotParseStateException()
            listOf()
        }
    }

    private fun parseMultipleStates(subLine: String): List<State> {
        val result: MutableList<State> = mutableListOf()
        subLine.split("|")
            .forEach { stateRegex ->
                stateRegex.split(";").also {
                    it.takeIf(::isValidState) ?: throw CanNotParseStateException()
                    result.add(State(it[ProjectColumnIndex.STATE_ID], it[ProjectColumnIndex.STATE_NAME]))
                }
            }
        return result
    }

    private fun parseOneState(subLine: String): List<State> {
        val listOfRegex: List<String> = subLine.split(";")
            .takeIf(::isValidState) ?: throw CanNotParseStateException()
        return listOf(State(listOfRegex[ProjectColumnIndex.STATE_ID], listOfRegex[ProjectColumnIndex.STATE_NAME]))
    }

    private fun isValidProject(projectRegex: List<String>): Boolean {
        return projectRegex.size == PROJECT_LINE_REGEX_NUMBERS
                && projectRegex[ProjectColumnIndex.ID] != ""
                && projectRegex[ProjectColumnIndex.NAME] != ""
    }

    private fun isValidState(stateRegex: List<String>): Boolean {
        return stateRegex.size == STATE_LINE_REGEX_NUMBERS
                && stateRegex[ProjectColumnIndex.STATE_ID] != ""
                && stateRegex[ProjectColumnIndex.STATE_NAME] != ""
    }

    private companion object {
        const val UNUSED_CHARACTER = 1
        const val PROJECT_LINE_REGEX_NUMBERS = 3
        const val STATE_LINE_REGEX_NUMBERS = 2
    }
}