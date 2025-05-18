package data.project.datasource.csv

import logic.exceptions.CanNotParseProjectException
import logic.exceptions.CanNotParseStateException
import logic.model.Project
import logic.model.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CsvProjectParser {


    fun parseProjectToString(project: Project): String {
        var res = project.projectId.toString() + "," + project.projectName + ","
        if (project.taskStates.isEmpty()) return res
        project.taskStates.forEach {
            res += it.stateId.toString() + ";" + it.stateName + "|"
        }
        return res.dropLast(UNUSED_CHARACTER)
    }

    fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf(::isValidProject)
            ?.let { projectRegex ->
                return Project(
                    Uuid.parse(projectRegex[ID]),
                    projectRegex[NAME],
                    parseStringToListOfState(projectRegex[STATES])
                )
            }
            ?: throw CanNotParseProjectException()
    }

    //ID,NAME,STATE   -->       ID/NAME|ID/NAME
    private fun parseStringToListOfState(subLine: String): List<TaskState> {
        return if (subLine.contains("|")) {
            parseMultipleStates(subLine)
        } else if (subLine.contains(";")) {
            parseOneState(subLine)
        } else {
            if (subLine.isNotEmpty()) throw CanNotParseStateException()
            listOf()
        }
    }

    private fun parseMultipleStates(subLine: String): List<TaskState> {
        val result: MutableList<TaskState> = mutableListOf()
        subLine.split("|")
            .forEach { stateRegex ->
                stateRegex.split(";").also {
                    it.takeIf(::isValidState) ?: throw CanNotParseStateException()
                    result.add(TaskState(Uuid.parse(it[STATE_ID]), it[STATE_NAME]))
                }
            }
        return result
    }

    private fun parseOneState(subLine: String): List<TaskState> {
        val listOfRegex: List<String> = subLine.split(";")
            .takeIf(::isValidState) ?: throw CanNotParseStateException()
        return listOf(TaskState(Uuid.parse(listOfRegex[STATE_ID]), listOfRegex[STATE_NAME]))
    }

    private fun isValidProject(projectRegex: List<String>): Boolean {
        return projectRegex.size == PROJECT_LINE_REGEX_NUMBERS
                && projectRegex[ID] != ""
                && projectRegex[NAME] != ""
    }

    private fun isValidState(stateRegex: List<String>): Boolean {
        return stateRegex.size == STATE_LINE_REGEX_NUMBERS
                && stateRegex[STATE_ID] != ""
                && stateRegex[STATE_NAME] != ""
    }

    private companion object {
        const val UNUSED_CHARACTER = 1
        const val PROJECT_LINE_REGEX_NUMBERS = 3
        const val STATE_LINE_REGEX_NUMBERS = 2
        const val ID = 0
        const val NAME = 1
        const val STATES = 2
        const val STATE_ID = 0
        const val STATE_NAME = 1
    }
}