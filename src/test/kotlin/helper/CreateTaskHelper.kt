package helper

import logic.model.Task
import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createTask(
    id : Uuid =Uuid.random(),
    userName :String = "",
    projectId :Uuid = Uuid.random(),
    stateId :Uuid = Uuid.random(),
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