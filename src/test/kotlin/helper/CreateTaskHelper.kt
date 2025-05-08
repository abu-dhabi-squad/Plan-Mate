package helper

import logic.model.Task
import java.time.LocalDate

fun createTask(id : String = "",
    userName :String = "",
    projectId :String = "",
    stateId :String = "",
    title :String= "",
    description :String = "",
    startDate :LocalDate = LocalDate.now(),
    endDate :LocalDate = LocalDate.now()
): Task {
    return Task(
        id = id,
        userName = userName,
        projectId = projectId,
        stateId = stateId,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate
    )
}