package squad.abudhabi.data.project.datasource

import squad.abudhabi.logic.exceptions.CanNotParseProjectException
import squad.abudhabi.logic.exceptions.CanNotParseStateException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectParser {

    fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf (::isValidProject)
            ?.let { projectRegex ->
                return Project(
                    projectRegex[ProjectColumnIndex.ID],
                    projectRegex[ProjectColumnIndex.NAME],
                    parseStringToListOfState(projectRegex[ProjectColumnIndex.STATES])
                )
            }
            ?: throw CanNotParseProjectException()
    }

    //ID,NAME,STATE   -->       ID-NAME|ID-NAME
    private fun parseStringToListOfState(subLine: String): List<State> {
        if (subLine.contains("|")) {
            val res: MutableList<State> = mutableListOf()
            subLine.split("|")
                .forEach { stateRegex ->
                    stateRegex.split("-").also {
                        it.takeIf(::isValidState) ?: throw CanNotParseStateException()
                        res.add(State(it[ProjectColumnIndex.STATE_ID], it[ProjectColumnIndex.STATE_NAME]))
                    }
                }
            return res
        } else {
            val listOfRegex: List<String> = subLine.split("-")
                .takeIf(::isValidState) ?: throw CanNotParseStateException()

            return listOf(State(listOfRegex[ProjectColumnIndex.STATE_ID], listOfRegex[ProjectColumnIndex.STATE_NAME]))
        }
    }

    private fun isValidProject(projectRegex: List<String>): Boolean {
        return projectRegex.isNotEmpty()
                && projectRegex.size == PROJECT_LINE_REGEX_NUMBERS
                && projectRegex[ProjectColumnIndex.ID] != ""
                && projectRegex[ProjectColumnIndex.NAME] != ""
                && projectRegex[ProjectColumnIndex.STATES] != ""
    }

    private fun isValidState(stateRegex: List<String>): Boolean {
        return stateRegex.isNotEmpty()
                && stateRegex.size == STATE_LINE_REGEX_NUMBERS
                && stateRegex[ProjectColumnIndex.STATE_ID] != ""
                && stateRegex[ProjectColumnIndex.STATE_NAME] != ""
    }

    companion object {
        const val PROJECT_LINE_REGEX_NUMBERS = 3
        const val STATE_LINE_REGEX_NUMBERS = 2
    }
}