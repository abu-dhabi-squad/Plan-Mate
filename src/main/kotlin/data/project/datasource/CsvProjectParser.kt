package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.Exceptions.CanNotParseProjectException
import squad.abudhabi.data.Exceptions.CanNotParseStateException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectParser {

    fun parseStringToProject(line: String): Project {
        line.split(",")
            .takeIf { it.isNotEmpty() && it.size == PROJECT_LINE_REGEX_NUMBERS }
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
                    stateRegex.split("-")
                        .takeIf { it.isNotEmpty() && it.size == STATE_LINE_REGEX_NUMBERS }
                        ?.apply {
                            res.add(State(this[ProjectColumnIndex.STATE_ID], this[ProjectColumnIndex.STATE_NAME]))
                        }
                        ?: throw CanNotParseStateException()
                }
            return res
        } else {
            val listOfRegex : List<String> = subLine.split("-")
                .takeIf { it.isNotEmpty() && it.size == STATE_LINE_REGEX_NUMBERS }
                ?: throw CanNotParseStateException()

            return listOf(State(listOfRegex[ProjectColumnIndex.STATE_ID],listOfRegex[ProjectColumnIndex.STATE_NAME]))
        }
    }

    companion object{
        const val PROJECT_LINE_REGEX_NUMBERS = 3
        const val STATE_LINE_REGEX_NUMBERS = 2
    }
}