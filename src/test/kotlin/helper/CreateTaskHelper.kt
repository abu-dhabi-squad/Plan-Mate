package helper

import logic.model.Task
import java.time.LocalDate
import java.util.UUID

fun createTask(
    id : UUID =UUID.randomUUID(),
    userName :String = "",
    projectId :UUID = UUID.randomUUID(),
    stateId :UUID = UUID.randomUUID(),
    title :String= "",
    description :String = "",
    startDate :LocalDate = LocalDate.now(),
    endDate :LocalDate = LocalDate.now()
): Task {
    return Task(
        taskId=id,
        username = userName,
        projectId = projectId,
        taskStateId = stateId,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate
    )
}