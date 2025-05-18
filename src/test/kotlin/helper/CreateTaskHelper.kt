package helper

import kotlinx.datetime.Clock
import logic.model.Task
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createTask(
    id: Uuid = Uuid.random(),
    userName: String = "",
    projectId: Uuid = Uuid.random(),
    stateId: Uuid = Uuid.random(),
    title: String = "",
    description: String = "",
    startDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    endDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
): Task {
    return Task(
        taskId = id,
        username = userName,
        projectId = projectId,
        taskStateId = stateId,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate
    )
}
